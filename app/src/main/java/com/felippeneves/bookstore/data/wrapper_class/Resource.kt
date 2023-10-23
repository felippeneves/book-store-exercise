package com.felippeneves.bookstore.data.wrapper_class

sealed class Resource<T>(
    val data: T? = null,
    val error: Exception? = null,
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: Exception?) : Resource<T>(error = error)
}
