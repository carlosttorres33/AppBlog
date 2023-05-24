package com.appblog.appblog.domain.home

import com.appblog.appblog.core.Result
import com.appblog.appblog.data.model.Post

interface HomeScreenRepo {

    suspend fun getLatestPosts(): Result<List<Post>>

}