package com.appblog.appblog.data.remote.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeScreenDataSource {

    suspend fun getLastestPost(): Result<List<Post>>{

        val postList = mutableListOf<Post>()

        withContext(Dispatchers.IO){
            val querySnapshot = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING).get().await()

            for (post in querySnapshot.documents) {

                post.toObject(Post::class.java)?.let { fbPost ->

                    val isLiked = FirebaseAuth.getInstance().currentUser?.let {safeUser ->

                        isPostLiked(post.id, safeUser.uid)

                    }

                    fbPost.apply {
                        created_at = post.getTimestamp(
                            "created_at",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )?.toDate()
                        id = post.id

                        if (isLiked != null){
                            liked = isLiked
                        }
                    }
                    postList.add(fbPost)
                }
            }
        }

        return Result.Succes(postList)

    }

    private suspend fun isPostLiked(postId: String, uid: String): Boolean{

        val post = FirebaseFirestore.getInstance().collection("postsLikes").document(postId).get().await()
        if (!post.exists()) return false
        val likeArray: List<String> = post.get("likes") as List<String>
        return  likeArray.contains(uid)
    }

    fun registerLikeButtonState(postId: String, liked: Boolean) {

        val increment = FieldValue.increment(1)
        val decrement = FieldValue.increment(-1)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val postRef = FirebaseFirestore.getInstance().collection("posts").document(postId)
        val postLikesRef = FirebaseFirestore.getInstance().collection("postsLikes").document(postId)

        val database = FirebaseFirestore.getInstance()

        database.runTransaction { transaction ->

            val snapshot = transaction.get(postRef)
            val likeCount = snapshot.getLong("likes")

            if (likeCount != null){
                if(likeCount >= 0){
                    if (liked){
                        if (transaction.get(postLikesRef).exists()){
                            transaction.update(postLikesRef, "likes", FieldValue.arrayUnion(uid))
                        }else{
                            transaction.set(postLikesRef, hashMapOf("likes" to arrayListOf(uid)), SetOptions.merge())
                        }

                        transaction.update(postRef, "likes", increment)

                    }else{
                        transaction.update(postRef, "likes", decrement)
                        transaction.update(postLikesRef, "likes", FieldValue.arrayRemove(uid))
                    }
                }
            }

        }.addOnFailureListener {
            throw Exception(it.message)
        }

    }

}