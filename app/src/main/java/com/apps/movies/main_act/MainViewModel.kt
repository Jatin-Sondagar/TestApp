package com.apps.movies.main_act

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.movies.helpers.Result
import com.apps.movies.repo.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val booksRepository: BooksRepository) : ViewModel() {
    val booksListLiveData: LiveData<Result<BooksModel>>
        get() = booksRepository.booksListLiveData

    fun getBooksList() {
        viewModelScope.launch(Dispatchers.IO) {
            if (booksListLiveData.value !is Result.Loading) {
                booksRepository.getBooksList()
            }
        }
    }
}