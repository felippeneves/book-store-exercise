package com.felippeneves.bookstore.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.felippeneves.bookstore.data.api.BookApi
import com.felippeneves.bookstore.utils.Constants

class BookPagingSource(
    private val api: BookApi,
): PagingSource<Int, Item>()  {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val page = params.key ?: 1
            val response = api.getBooks(query = Constants.BOOK_QUERY_DEFAULT, startIndex = page,
                maxResults = Constants.MAX_RESULTS_DEFAULT)

            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.items.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}