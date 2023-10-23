package com.felippeneves.bookstore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.annotation.Nullable

@Entity(tableName = "TB_FAVORITE_BOOK")
data class FavoriteBook(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String,
    @ColumnInfo(name = "TITLE")
    @Nullable
    val title: String? = null,
    @ColumnInfo(name = "SMALL_THUMBNAIL")
    @Nullable
    val smallThumbnail: String? = null,
    @ColumnInfo(name = "CREATED_AT")
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
)