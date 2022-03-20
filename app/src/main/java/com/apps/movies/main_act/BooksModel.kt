package com.apps.movies.main_act

data class BooksModel(
    val copyright: String,
    val last_modified: String,
    val num_results: Int,
    val results: Results,
    val status: String
)

data class Results(
    val books: List<Book>,
    val display_name: String,
    val list_name: String,
    val list_name_encoded: String,
)

data class Book(
    val amazon_product_url: String,
    val author: String,
    val book_image: String,
    val book_image_height: Int,
    val book_image_width: Int,
    val book_uri: String,
    val buy_links: List<BuyLink>,
    val contributor: String,
    val contributor_note: String,
    val description: String,
    val price: String,
    val publisher: String,
    val title: String,
)

data class BuyLink(
    val name: String,
    val url: String
)