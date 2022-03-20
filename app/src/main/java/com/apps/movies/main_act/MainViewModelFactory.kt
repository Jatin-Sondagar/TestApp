package com.apps.movies.main_act

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.movies.repo.BooksRepository

class MainViewModelFactory(val booksRepository: BooksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(booksRepository) as T
    }
}