package com.appblog.appblog.domain.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {

    suspend fun getLatestPosts(): Flow<Result<List<Post>>>

}