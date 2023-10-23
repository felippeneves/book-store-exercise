package com.felippeneves.bookstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.felippeneves.bookstore.R

@Composable
fun Favorite(favorite: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favorite_icon),
            tint = if (favorite) Color.Red else Color.Black
        )
        Text(
            text = stringResource(id = R.string.favorite),
            color = if (favorite) Color.Red else Color.Black,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}