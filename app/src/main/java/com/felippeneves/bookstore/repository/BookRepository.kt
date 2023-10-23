package com.felippeneves.bookstore.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.felippeneves.bookstore.data.wrapper_class.Resource
import com.felippeneves.bookstore.model.Item
import com.felippeneves.bookstore.data.api.BookApi
import com.felippeneves.bookstore.data.database.dao.FavoriteBookDao
import com.felippeneves.bookstore.model.BookPagingSource
import com.felippeneves.bookstore.model.FavoriteBook
import com.felippeneves.bookstore.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val api: BookApi,
    private val dao: FavoriteBookDao
) {
    fun getBooks() = Pager(
        config = PagingConfig(
            pageSize = Constants.MAX_RESULTS_DEFAULT,
        ),
        pagingSourceFactory = {
            BookPagingSource(api)
        }
    ).flow

    suspend fun getBookDetails(bookId: String): Resource<Item> {
        return try {
            val book = api.getBookDetails(bookId)
            Resource.Success(data = book)
        } catch (e: Exception) {
            Resource.Error(error = e)
        }
    }

    suspend fun addFavoriteBook(favoriteBook: FavoriteBook): Resource<Boolean> {
        return try {
            val result: Boolean = dao.insert(favoriteBook) > 0
            Resource.Success(data = result)
        } catch (e: Exception) {
            Resource.Error(error = e)
        }
    }

    suspend fun deleteFavoriteBook(favoriteBook: FavoriteBook): Resource<Boolean> {
        return try {
            Resource.Success(data = dao.delete(favoriteBook) > 0)
        } catch (e: Exception) {
            Resource.Error(error = e)
        }
    }

    suspend fun isFavoriteBook(id: String): Resource<Boolean> {
        return try {
            Resource.Success(data = dao.countById(id) > 0)
        } catch (e: Exception) {
            Resource.Error(error = e)
        }
    }

    fun getAllFavoriteBooks(): Flow<List<FavoriteBook>> =
        dao.getAll()
}