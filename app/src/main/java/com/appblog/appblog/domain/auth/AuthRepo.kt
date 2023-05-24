package com.appblog.appblog.domain.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepo {

    suspend fun  singIn(email: String, password: String): FirebaseUser?
    suspend fun singUp(username: String, email: String, password: String): FirebaseUser?

}