package com.felippeneves.bookstore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackArrowClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showBackButton) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "arrow_back",
                        modifier = Modifier
                            .clickable {
                                onBackArrowClicked.invoke()
                            },
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))

                Text(
                    text = title,
                    color = Color.White,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        },
        actions = {},
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Blue.copy(alpha = 0.7f)
        )
    )
}