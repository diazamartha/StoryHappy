package com.example.storyhappy.data

sealed class Result<out R> private constructor() {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
    class Idle<T> : Result<T>()
}
