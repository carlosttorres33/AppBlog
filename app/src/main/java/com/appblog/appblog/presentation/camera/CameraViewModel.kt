package com.appblog.appblog.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.appblog.appblog.core.Result
import com.appblog.appblog.domain.auth.AuthRepo
import com.appblog.appblog.domain.camera.CameraRepo
import com.appblog.appblog.presentation.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers

class CameraViewModel(private val repo: CameraRepo): ViewModel() {

    fun uploadPhoto(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Succes(repo.uploadPhoto(imageBitmap, description)))
        }catch (e: Exception){
            emit(Result.Faliure(e))
        }
    }

}

class CameraViewModelFactory(private val repo: CameraRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CameraRepo::class.java).newInstance(repo)
    }
}