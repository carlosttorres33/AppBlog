package com.appblog.appblog.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.appblog.appblog.core.Result
import com.appblog.appblog.domain.auth.AuthRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class AuthViewModel(private var repo: AuthRepo): ViewModel() {

    fun singIn(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Succes(repo.singIn(email,password)))
        }catch (e: Exception){
            emit(Result.Faliure(e))
        }
    }

    fun singUp(username:String, email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Succes(repo.singUp(username,email,password)))
        }catch (e: Exception){
            emit(Result.Faliure(e))
        }
    }

    fun updateUserProfile(imageBitmap: Bitmap, username: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Succes(repo.updateProfile(imageBitmap, username)))
        }catch (e: Exception){
            emit(Result.Faliure(e))
        }
    }

}

class AuthViewModelFactory(private val repo: AuthRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }
}