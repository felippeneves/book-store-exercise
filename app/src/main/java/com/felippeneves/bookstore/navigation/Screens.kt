package com.felippeneves.bookstore.navigation

import java.lang.IllegalArgumentException

enum class Screens {
    BooksHomeScreen,
    BookDetailsScreen;

    companion object {
        fun fromRoute(route: String?): Screens = when (route?.substringBefore("/")) {
            BooksHomeScreen.name -> BooksHomeScreen
            BookDetailsScreen.name -> BookDetailsScreen
            null -> BooksHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}