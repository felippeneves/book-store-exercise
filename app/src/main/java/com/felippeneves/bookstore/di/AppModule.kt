package com.felippeneves.bookstore.di

import android.content.Context
import androidx.room.Room
import com.felippeneves.bookstore.data.api.BookApi
import com.felippeneves.bookstore.data.database.BookStoreDatabase
import com.felippeneves.bookstore.data.database.dao.FavoriteBookDao
import com.felippeneves.bookstore.repository.BookRepository
import com.felippeneves.bookstore.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //region Database

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): BookStoreDatabase =
        Room.databaseBuilder(
            context,
            BookStoreDatabase::class.java,
            BookStoreDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    //endregion

    //region API's

    @Singleton
    @Provides
    fun provideBookApi(): BookApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }

    //endregion

    //region DAO's

    @Singleton
    @Provides
    fun provideFavoriteBookDao(bookStoreDatabase: BookStoreDatabase): FavoriteBookDao =
        bookStoreDatabase.FavoriteBookDao()

    //endregion

    //region Repositories

    @Singleton
    @Provides
    fun provideBookRepository(api: BookApi, dao: FavoriteBookDao) = BookRepository(api, dao)

    //endregion
}