package com.appblog.appblog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.appblog.appblog.core.Result
import com.appblog.appblog.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class HomeScreenViewModel(private val repo: HomeScreenRepo): ViewModel() {

    fun fetchLastestPosts() = liveData(Dispatchers.IO){

        emit(Result.Loading())

        kotlin.runCatching {
            repo.getLatestPosts()
        }.onSuccess { postList ->
            emit(postList)
        }.onFailure {
            emit(Result.Faliure(Exception(it.message)))
        }

    }

    fun registerLikeButtonState(postId: String, liked : Boolean)= liveData(viewModelScope.coroutineContext + Dispatchers.Main) {

        emit(Result.Loading())
        kotlin.runCatching {
            repo.registerLikeButtonState(postId, liked)
        }.onSuccess {
            emit(Result.Succes(Unit))
        }.onFailure {throwable ->
            emit(Result.Faliure(Exception(throwable.message)))
        }

    }

}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}