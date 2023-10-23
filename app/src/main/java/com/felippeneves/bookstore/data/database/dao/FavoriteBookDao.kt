package com.felippeneves.bookstore.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.felippeneves.bookstore.data.database.BaseDao
import com.felippeneves.bookstore.model.FavoriteBook
import kotlinx.coroutines.flow.Flow

/**
 * [Room] DAO for [FavoriteBook] related operations.
 */
@Dao
abstract class FavoriteBookDao : BaseDao<FavoriteBook> {
    @Query(
        """   
            SELECT *   
            FROM TB_FAVORITE_BOOK
        """
    )
    abstract fun getAll(): Flow<List<FavoriteBook>>

    @Query(
        """   
            SELECT *      
            FROM TB_FAVORITE_BOOK
            WHERE ID = :id
        """
    )
    abstract suspend fun getById(id: String): FavoriteBook

    @Query(
        """   
            DELETE      
            FROM TB_FAVORITE_BOOK
        """
    )
    abstract suspend fun deleteAll()

    @Query(
        """   
            SELECT COUNT(1)     
            FROM TB_FAVORITE_BOOK
            WHERE ID = :id
        """
    )
    abstract suspend fun countById(id: String): Int
}