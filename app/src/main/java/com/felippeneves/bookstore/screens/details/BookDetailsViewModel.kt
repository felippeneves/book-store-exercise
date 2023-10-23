package com.felippeneves.bookstore.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felippeneves.bookstore.data.wrapper_class.Resource
import com.felippeneves.bookstore.model.FavoriteBook
import com.felippeneves.bookstore.model.Item
import com.felippeneves.bookstore.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    private var _book: Item? by mutableStateOf(null)
    val book
        get() = _book

    private var _loading: Boolean by mutableStateOf(false)
    val loading
        get() = _loading

    private var _error: Exception? by mutableStateOf(null)
    val error
        get() = _error

    fun getBookDetails(bookId: String) {
        viewModelScope.launch {
            if (bookId.isEmpty())
                return@launch

            try {
                _loading = true
                when (val response = repository.getBookDetails(bookId)) {
                    is Resource.Success -> {
                        _book = response.data!!
                        _loading = false
                    }

                    is Resource.Error -> {
                        _error = response.error!!
                        _loading = false
                    }

                    else -> {
                        _loading = false
                    }
                }
            } catch (e: Exception) {
                _error = e
                _loading = false
            }
        }
    }

    fun addFavoriteBook(favoriteBook: FavoriteBook, onComplete: (error: Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                when (val response = repository.addFavoriteBook(favoriteBook)) {
                    is Resource.Success -> {
                        onComplete(null)
                    }
                    is Resource.Error -> {
                        onComplete(response.error!!)
                    }
                }
            } catch (e: Exception) {
                onComplete(e)
            }
        }
    }

    fun deleteFavoriteBook(favoriteBook: FavoriteBook, onComplete: (error: Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                when (val response = repository.deleteFavoriteBook(favoriteBook)) {
                    is Resource.Success -> {
                        onComplete(null)
                    }
                    is Resource.Error -> {
                        onComplete(response.error!!)
                    }
                }
            } catch (e: Exception) {
                onComplete(e)
            }
        }
    }

    fun isFavoriteBook(id: String, onComplete: (favorite: Boolean, error: Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                when (val response = repository.isFavoriteBook(id)) {
                    is Resource.Success -> {
                        onComplete(response.data!!, null)
                    }
                    is Resource.Error -> {
                        onComplete(false, response.error!!)
                    }
                }
            } catch (e: Exception) {
                onComplete(false, e)
            }
        }
    }
}
