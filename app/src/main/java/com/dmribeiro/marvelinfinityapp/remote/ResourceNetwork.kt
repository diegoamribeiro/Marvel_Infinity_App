package com.dmribeiro.marvelinfinityapp.remote

sealed class ResourceNetwork<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): ResourceNetwork<T>(data)
    class Error<T>(message: String, data: T? = null): ResourceNetwork<T>(data, message)
    class Loading<T>: ResourceNetwork<T>()
}