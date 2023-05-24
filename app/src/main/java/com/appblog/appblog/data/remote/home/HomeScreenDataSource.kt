package com.appblog.appblog.data.remote.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLastestPost(): Result<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()

        for (post in querySnapshot.documents){

            post.toObject(Post::class.java)?.let { fbPost ->
                postList.add(fbPost)
            }

        }

        return Result.Succes(postList)

    }

}