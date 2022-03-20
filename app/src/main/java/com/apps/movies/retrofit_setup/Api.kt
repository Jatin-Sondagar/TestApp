package com.apps.movies.retrofit_setup

import com.apps.movies.main_act.BooksModel
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    companion object {
        val BASE_URL = "https://api.nytimes.com/svc/books/v3/lists/current/"
    }

    @GET("hardcover-fiction.json?api-key=3lWBZAA4C9I3jUVOCDaX91SWkKh9s0ja")
    suspend fun getBooksList(): Response<BooksModel>
}