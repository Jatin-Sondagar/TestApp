package com.apps.movies.helpers

sealed class Result<T>(
    val apiState: ApiState = ApiState.LOADING,
    val data: T? = null,
    val errorMessage: String? = null) {

    class Loading<T>(apiState: ApiState) : Result<T>(apiState = apiState)
    class Success<T>(apiState: ApiState, data: T?) :
        Result<T>(apiState = apiState, data = data)

    class Failure<T>(apiState: ApiState, errorMessage: String?) :
        Result<T>(apiState = apiState, errorMessage = errorMessage)
}

enum class ApiState {
    LOADING,
    SUCCESS,
    ERROR
}
