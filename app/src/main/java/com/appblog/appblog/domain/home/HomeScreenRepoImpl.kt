package com.appblog.appblog.domain.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post
import com.appblog.appblog.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.flow.Flow

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
    override suspend fun getLatestPosts(): Flow<Result<List<Post>>> = dataSource.getLastestPost()

}