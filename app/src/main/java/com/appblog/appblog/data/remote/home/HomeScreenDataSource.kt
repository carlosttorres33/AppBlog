package com.appblog.appblog.data.remote.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLastestPost(): Result<List<Post>> {
        val postList = mutableListOf<Post>()
        //Se obtiene la lista de posts, una vez obtenida la lista se ordenan de manera decendente para ver los mas nuevos primero, desde firebase
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").orderBy("created_at", Query.Direction.DESCENDING).get().await()

        for (post in querySnapshot.documents){

            post.toObject(Post::class.java)?.let { fbPost ->
                fbPost.apply { created_at = post.getTimestamp("created_at", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)?.toDate() }
                postList.add(fbPost)
            }

        }

        return Result.Succes(postList)

    }

}