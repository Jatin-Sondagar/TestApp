package com.apps.movies.main_act

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.apps.movies.R
import com.apps.movies.databinding.BookListLoadingBinding
import com.apps.movies.databinding.BooksListItemBinding
import com.apps.movies.helpers.ApiState
import com.bumptech.glide.Glide

class BooksAdapter(val monItemClick: MainActivity.onBookListItemClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var apiState: ApiState = ApiState.LOADING
    private var list = emptyList<Book>()
    private var filterList = emptyList<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == -1) {
            val binding = BookListLoadingBinding.inflate(inflater, parent, false)
            return EmptyViewHolder(binding)
        } else {
            val binding = BooksListItemBinding.inflate(inflater, parent, false)
            return MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            with(holder) { bind(filterList.get(position)) }
        } else if (holder is EmptyViewHolder) holder.bind()
    }

    override fun getItemCount(): Int {
        return if (apiState == ApiState.SUCCESS) filterList.size else 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (apiState == ApiState.SUCCESS) position else -1
    }

    fun setData(apiState: ApiState, data: BooksModel?, errorMessage: String?) {
        this.apiState = apiState
        when (apiState) {
            ApiState.SUCCESS -> if (data != null) {
                list = data.results.books
                filterList = list
            }
            else -> {
                Log.e("api state : ", apiState.name)
            }
        }
        notifyDataSetChanged()
    }

    fun sortList() {
        filterList = filterList.sortedWith(compareBy(Book::title, Book::title))
        notifyDataSetChanged()
    }

    fun filter(filter: String) {
        list.filter { book -> book.title.lowercase().contains(filter) }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: BooksListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context

        init {
            context = itemView.context
            itemView.setOnClickListener { monItemClick.onItemClick(filterList.get(adapterPosition)) }
        }

        fun bind(book: Book) {
            with(binding) {
                Glide.with(context)
                    .load(book.book_image)
                    .placeholder(R.drawable.loading)
                    .placeholder(R.drawable.error)
                    .centerCrop()
                    .into(ivBook)
                val title = "<b>Title : </b>" + book.title
                tvTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)

                val desc = "<b>Desc : </b>" + book.description
                tvDesc.text = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)

                val author = "<b>Author : </b>" + book.author
                tvAuthor.text = HtmlCompat.fromHtml(author, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    inner class EmptyViewHolder(val binding: BookListLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var context: Context = itemView.context
        var isLoading = false

        init {
            context = itemView.context
            isLoading = apiState == ApiState.LOADING
        }

        fun bind() {
            with(binding) {
                pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
                val msg =
                    if (isLoading) context.resources.getString(R.string._fetching_data)
                    else context.resources.getString(R.string._smthng_went_wrong)
                tvMessage.text = msg
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filter = constraint.toString()
                if (filter.isEmpty()) {
                    filterList = list
                } else {
                    val resultList = mutableListOf<Book>()
                    for (row in list) {
                        if (row.title.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(filter: CharSequence?, results: FilterResults?) {
                filterList = results?.values as List<Book>
                notifyDataSetChanged()
            }
        }
    }
}