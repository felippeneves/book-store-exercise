package com.felippeneves.bookstore.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.felippeneves.bookstore.R
import com.felippeneves.bookstore.components.AppBar
import com.felippeneves.bookstore.components.Loading
import com.felippeneves.bookstore.components.ShowToast
import com.felippeneves.bookstore.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksHomeScreen(
    navController: NavHostController,
    viewModel: BooksHomeViewModel
) {
    val showFavorites = remember { mutableStateOf(false) }

    Scaffold(topBar = {
        AppBar(title = stringResource(id = R.string.books))
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = showFavorites.value,
                        onCheckedChange = { isChecked -> showFavorites.value = isChecked }
                    )
                    Text(stringResource(id = R.string.ShowFavorites))
                }

                if (showFavorites.value) {
                    BookListFavorites(navController, viewModel)
                } else {
                    BookListPagination(navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun BookListPagination(
    navController: NavHostController,
    viewModel: BooksHomeViewModel
) {
    val bookList = viewModel.getBooks().collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {

        items(bookList.itemCount) { index ->
            bookList[index]?.let { book ->
                val painter =
                    if (book.volumeInfo.imageLinks != null) rememberImagePainter(data = book.volumeInfo.imageLinks.smallThumbnail)
                    else painterResource(R.drawable.ic_hide_image)

                BookItem(
                    navController = navController,
                    bookId = book.id,
                    painter = painter,
                    title = book.volumeInfo.title
                )
            }
        }

        //First Load
        when (val state = bookList.loadState.refresh) {
            is LoadState.Error -> {
                item {
                    ShowToast(
                        context = LocalContext.current,
                        message = stringResource(id = R.string.unexpected_failure_load_list)
                    )
                }
            }

            is LoadState.Loading -> {
                item {
                    Loading()
                }
            }

            else -> {}
        }

        //Pagination
        when (val state = bookList.loadState.append) {
            is LoadState.Error -> {
                item {
                    ShowToast(
                        context = LocalContext.current,
                        message = stringResource(id = R.string.unexpected_failure_load_list)
                    )
                }
            }

            is LoadState.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            else -> {}
        }

    }
}

@Composable
fun BookListFavorites(
    navController: NavHostController,
    viewModel: BooksHomeViewModel
) {
    val favoriteBookList = viewModel.getFavoriteBooks().collectAsState(initial = listOf()).value

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(favoriteBookList) { favoriteBook ->
            val painter =
                if (favoriteBook.smallThumbnail != null) rememberImagePainter(data = favoriteBook.smallThumbnail)
                else painterResource(R.drawable.ic_hide_image)

            BookItem(
                navController = navController,
                bookId = favoriteBook.id,
                painter = painter,
                title = favoriteBook.title.toString()
            )
        }
    }
}

@Composable
fun BookItem(
    navController: NavHostController,
    bookId: String,
    painter: Painter,
    title: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(4.dp)
            .clickable {
                navController.navigate(Screens.BookDetailsScreen.name + "/${bookId}")
            },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .height(70.dp)
                    .onSizeChanged {},
                painter = painter,
                contentScale = ContentScale.FillBounds,
                contentDescription = stringResource(id = R.string.book_image)
            )

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = title,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}