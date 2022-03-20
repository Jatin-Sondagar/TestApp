package com.apps.movies.next_act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.apps.movies.R
import com.apps.movies.databinding.ActivityBookDetailsBinding
import com.apps.movies.main_act.Book
import com.bumptech.glide.Glide
import com.google.gson.Gson

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_details)

        binding.ivBack.setOnClickListener { onBackPressed() }
        val book = Gson().fromJson(intent.getStringExtra("obj_book"), Book::class.java)
        if (book != null) {
            with(binding) {
                Glide.with(root.context)
                    .load(book.book_image)
                    .placeholder(R.drawable.loading)
                    .placeholder(R.drawable.error)
                    .centerCrop()
                    .into(binding.ivBook)

                val title = "<b>Title : </b>" + book.title
                tvTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)

                val desc = "<b>Desc : </b>" + book.description
                tvDesc.text = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)

                val author = "<b>Author : </b>" + book.author
                tvAuthor.text = HtmlCompat.fromHtml(author, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}