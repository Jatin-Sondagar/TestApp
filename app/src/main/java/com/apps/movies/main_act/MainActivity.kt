package com.apps.movies.main_act

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.movies.R
import com.apps.movies.databinding.ActivityMainBinding
import com.apps.movies.helpers.ApiState
import com.apps.movies.next_act.BookDetailsActivity
import com.apps.movies.repo.BooksRepository
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private var booksAdapter = BooksAdapter(object : onBookListItemClick {
        override fun onItemClick(book: Book) {
            val intent = Intent(binding.root.context, BookDetailsActivity::class.java)
            intent.putExtra("obj_book", Gson().toJson(book))
            startActivity(intent)
        }
    })
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViewModel()
        setupViews()
        setupObservers()
        mainViewModel.getBooksList()
    }

    private fun setupViews() {
        setupRecyclerView()
        binding.btnSort.setOnClickListener { booksAdapter.sortList() }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                booksAdapter.filter.filter(text)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBookList.layoutManager = llm
        binding.rvBookList.adapter = booksAdapter
    }

    private fun setupObservers() {
        mainViewModel.booksListLiveData.observe(this) {
            binding.btnSort.visibility = if (it.apiState == ApiState.SUCCESS) View.VISIBLE else View.GONE
            binding.etSearch.visibility = binding.btnSort.visibility
            booksAdapter.setData(it.apiState, it.data, it.errorMessage)
        }
    }

    private fun setupViewModel() {
        val booksRepository = BooksRepository()
        val mainViewModelFactory = MainViewModelFactory(booksRepository)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
    }

    interface onBookListItemClick {
        fun onItemClick(book: Book)
    }
}