package com.felippeneves.bookstore.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.felippeneves.bookstore.model.FavoriteBook
import com.felippeneves.bookstore.model.Item
import com.felippeneves.bookstore.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class BooksHomeViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    fun getBooks(): Flow<PagingData<Item>> = repository.getBooks().cachedIn(viewModelScope)

    fun getFavoriteBooks(): Flow<List<FavoriteBook>> = repository.getAllFavoriteBooks().distinctUntilChanged()
}