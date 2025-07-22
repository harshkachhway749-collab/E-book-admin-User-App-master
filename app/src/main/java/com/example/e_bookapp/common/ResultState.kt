package com.example.e_bookapp.common

sealed class ResultState<out T> {

    data class Success<out T>(val data: T) : ResultState<T>()

    data class Eroor(val exception: String) : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()

}