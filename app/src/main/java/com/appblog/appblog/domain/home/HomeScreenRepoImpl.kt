package com.appblog.appblog.domain.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post
import com.appblog.appblog.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
    override suspend fun getLatestPosts(): Result<List<Post>> = dataSource.getLastestPost()
    override suspend fun registerLikeButtonState(postId: String, liked: Boolean) = dataSource.registerLikeButtonState(postId, liked)

}