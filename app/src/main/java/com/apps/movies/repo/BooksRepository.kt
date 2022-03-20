package com.apps.movies.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.movies.helpers.ApiState
import com.apps.movies.helpers.Result
import com.apps.movies.retrofit_setup.Api
import com.apps.movies.retrofit_setup.RetrofitHelper
import com.apps.movies.main_act.BooksModel

class BooksRepository {
    private val _mutableBooksList: MutableLiveData<Result<BooksModel>> = MutableLiveData()
    val booksListLiveData: LiveData<Result<BooksModel>>
        get() = _mutableBooksList

    suspend fun getBooksList() {
        _mutableBooksList.postValue(Result.Loading(apiState = ApiState.LOADING))
        val booksModel = RetrofitHelper.getInstance(Api.BASE_URL).create(Api::class.java)
            .getBooksList()
        if (booksModel != null && booksModel.isSuccessful && booksModel.body() != null) {
            _mutableBooksList.postValue(
                Result.Success(apiState = ApiState.SUCCESS, data = booksModel.body()))
        } else {
            _mutableBooksList.postValue(Result.Failure(apiState = ApiState.ERROR,
                errorMessage = "Something went wrong."))
        }
    }
}