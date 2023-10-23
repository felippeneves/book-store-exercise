package com.felippeneves.bookstore.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felippeneves.bookstore.data.database.dao.FavoriteBookDao
import com.felippeneves.bookstore.model.FavoriteBook

@Database(
    entities = [
        FavoriteBook::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BookStoreDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "book_store_database.db3"
    }

    abstract fun FavoriteBookDao(): FavoriteBookDao
}