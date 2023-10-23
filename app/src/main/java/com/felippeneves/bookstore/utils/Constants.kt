package com.felippeneves.bookstore.utils

object Constants {
    //https://www.googleapis.com/books/v1/volumes?q=ios&maxResults=1&startIndex=19
    const val BASE_URL = "https://www.googleapis.com/books/v1/"
    const val VOLUMES_API = "volumes"
    const val SEARCH_BOOKS_PARAM = "q"
    const val MAX_RESULTS_PARAM = "maxResults"
    const val START_INDEX_PARAM = "startIndex"

    const val BOOK_ID_PARAMETER = "{bookId}"
    const val BOOK_QUERY_DEFAULT = "mobile development"
    const val MAX_RESULTS_DEFAULT = 20

    const val BOOK_ID_ARGUMENT = "bookId"

    const val BOOK_FOR_SALE = "FOR_SALE"
}