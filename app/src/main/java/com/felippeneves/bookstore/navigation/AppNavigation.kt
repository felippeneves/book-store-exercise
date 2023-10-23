package com.felippeneves.bookstore.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.felippeneves.bookstore.screens.details.BookDetailsScreen
import com.felippeneves.bookstore.screens.details.BookDetailsViewModel
import com.felippeneves.bookstore.screens.home.BooksHomeScreen
import com.felippeneves.bookstore.screens.home.BooksHomeViewModel
import com.felippeneves.bookstore.utils.Constants

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.BooksHomeScreen.name) {
        composable(Screens.BooksHomeScreen.name) {
            val booksHomeViewModel = hiltViewModel<BooksHomeViewModel>()
            BooksHomeScreen(
                navController = navController,
                viewModel = booksHomeViewModel
            )
        }

        val detailScreens = Screens.BookDetailsScreen.name
        composable(
            route = "$detailScreens/{${Constants.BOOK_ID_ARGUMENT}}",
            arguments = listOf(navArgument(Constants.BOOK_ID_ARGUMENT) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(Constants.BOOK_ID_ARGUMENT).let { bookId ->
                val bookDetailsViewModel = hiltViewModel<BookDetailsViewModel>()
                BookDetailsScreen(
                    navController = navController,
                    viewModel = bookDetailsViewModel,
                    bookId = bookId.toString()
                )
            }
        }
    }
}