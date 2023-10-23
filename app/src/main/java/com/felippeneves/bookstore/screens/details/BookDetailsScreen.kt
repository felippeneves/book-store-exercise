package com.felippeneves.bookstore.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.felippeneves.bookstore.R
import com.felippeneves.bookstore.components.AppBar
import com.felippeneves.bookstore.components.Loading
import com.felippeneves.bookstore.components.ShowToast
import com.felippeneves.bookstore.components.Favorite
import com.felippeneves.bookstore.model.FavoriteBook
import com.felippeneves.bookstore.model.Item
import com.felippeneves.bookstore.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    viewModel: BookDetailsViewModel,
    bookId: String
) {
    val book = viewModel.book
    val loading = viewModel.loading
    val error = viewModel.error
    val showToast = remember { mutableStateOf("") }
    val context = LocalContext.current
    val favorite = remember { mutableStateOf(false) }

    viewModel.isFavoriteBook(id = bookId) { isFavorite, error ->
        if (error == null) {
            favorite.value = isFavorite
        } else {
            showToast.value = context.getString(R.string.unexpected_failure)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getBookDetails(bookId)
    }

    Scaffold(topBar = {
        AppBar(
            title = stringResource(id = R.string.book_details),
            showBackButton = true,
        ) {
            navController.popBackStack()
        }
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (loading) {
                    Loading()
                } else {
                    if (error == null) {
                        BookDetails(book, viewModel, favorite.value)
                    } else {
                        ShowToast(
                            context = LocalContext.current,
                            message = stringResource(id = R.string.unexpected_failure_load_book)
                        )
                    }
                }
            }
        }

        if (showToast.value.isNotEmpty()) {
            ShowToast(
                context = context,
                message = showToast.value
            )
            showToast.value = ""
        }
    }
}

@Composable
fun BookDetails(
    book: Item?,
    viewModel: BookDetailsViewModel,
    favorite: Boolean
) {
    val bookInformation = book?.volumeInfo
    val bookForSale = Constants.BOOK_FOR_SALE.equals(book?.saleInfo?.saleability)
    val favoriteBook = remember { mutableStateOf(favorite) }
    val description = HtmlCompat.fromHtml(
        bookInformation?.description.toString(),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
    val uriHandler = LocalUriHandler.current
    val showToast = remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(0.8f),
                    text = bookInformation?.title.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
                IconButton(
                    modifier = Modifier.weight(0.2f),
                    onClick = {

                        if (!favoriteBook.value) {
                            val smallThumbnail = book?.volumeInfo?.imageLinks?.smallThumbnail

                            viewModel.addFavoriteBook(
                                FavoriteBook(
                                    id = book!!.id,
                                    smallThumbnail = smallThumbnail,
                                    title = book.volumeInfo.title
                                )
                            ) { error ->
                                if (error == null) {
                                    showToast.value =
                                        context.getString(R.string.book_success_favorite)
                                } else {
                                    showToast.value = context.getString(R.string.book_fail_favorite)
                                }
                            }
                        } else {
                            viewModel.deleteFavoriteBook(FavoriteBook(id = book!!.id)) { error ->
                                if (error == null) {
                                    showToast.value =
                                        context.getString(R.string.book_success_unfavorite)
                                } else {
                                    showToast.value =
                                        context.getString(R.string.book_fail_unfavorite)
                                }
                            }
                        }

                        favoriteBook.value = !favoriteBook.value
                    }) {
                    Favorite(favoriteBook.value)
                }
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.author),
                fontWeight = FontWeight.Bold
            )
            Text(text = bookInformation?.authors.toString())

            if (bookForSale) {

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(id = R.string.buy_link),
                    fontWeight = FontWeight.Bold
                )
                ClickableText(
                    text = AnnotatedString(book?.saleInfo?.buyLink.toString()),
                    style = TextStyle(
                        color = colorResource(R.color.link_color),
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline
                    ),
                    onClick = {
                        uriHandler.openUri(book?.saleInfo?.buyLink.toString())
                    })
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.description),
                fontWeight = FontWeight.Bold
            )
            Text(text = description)

            if (showToast.value.isNotEmpty()) {
                ShowToast(
                    context = context,
                    message = showToast.value
                )
                showToast.value = ""
            }
        }
    }
}
