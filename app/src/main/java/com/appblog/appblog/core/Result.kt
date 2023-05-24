package com.appblog.appblog.core

import java.lang.Exception

sealed class Result<out T> {

    class Loading<out T>: Result<T>()

    data class Succes<out T>(val data: T):Result<T>()

    data class Faliure(val exception: Exception):Result<Nothing>()


}