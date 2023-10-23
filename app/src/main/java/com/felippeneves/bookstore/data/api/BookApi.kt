package com.felippeneves.bookstore.data.api

import com.felippeneves.bookstore.model.Book
import com.felippeneves.bookstore.model.Item
import com.felippeneves.bookstore.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {
    @GET(Constants.VOLUMES_API)
    suspend fun getBooks(
        @Query(Constants.SEARCH_BOOKS_PARAM) query: String,
        @Query(Constants.START_INDEX_PARAM) startIndex: Int,
        @Query(Constants.MAX_RESULTS_PARAM) maxResults: Int = Constants.MAX_RESULTS_DEFAULT
    ): Book

    @GET(Constants.VOLUMES_API + "/" + Constants.BOOK_ID_PARAMETER)
    suspend fun getBookDetails(@Path(Constants.BOOK_ID_ARGUMENT) bookId: String): Item
}