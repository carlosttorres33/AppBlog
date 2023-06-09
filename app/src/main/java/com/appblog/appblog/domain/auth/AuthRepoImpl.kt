package com.appblog.appblog.domain.auth

import android.graphics.Bitmap
import com.appblog.appblog.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val dataSource: AuthDataSource) : AuthRepo {

    override suspend fun singIn(email: String, password: String): FirebaseUser? =
        dataSource.singIn(email, password)

    override suspend fun singUp(username: String, email: String, password: String): FirebaseUser? =
        dataSource.singUp(username, email, password)

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) = dataSource.updateUserProfile(imageBitmap, username)

}