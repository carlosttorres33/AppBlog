package com.appblog.appblog.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.appblog.appblog.core.Result
import kotlinx.coroutines.Dispatchers

class CameraViewModel(private val repo: CameraRepo):ViewModel {

    fun uploadPhoto(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.Main) {
        emit(Result.Loading())
        try {

        }catch (e: Exception){
            emit(Result.Faliure)
        }
    }
}