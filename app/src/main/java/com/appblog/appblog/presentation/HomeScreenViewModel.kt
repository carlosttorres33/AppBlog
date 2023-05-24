package com.appblog.appblog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.appblog.appblog.core.Result
import com.appblog.appblog.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers

class HomeScreenViewModel(private val repo: HomeScreenRepo): ViewModel() {

    fun fetchLastestPosts() = liveData(Dispatchers.IO){

        emit(Result.Loading())

        try {

            emit(repo.getLatestPosts())

        }catch (e:Exception){

            emit(Result.Faliure(e))

        }

    }

}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}