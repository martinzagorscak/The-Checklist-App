package com.example.thechecklistapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thechecklistapp.R
import com.example.thechecklistapp.ui.components.IconButton
import com.example.thechecklistapp.ui.components.TopBar

@Composable
fun DetailedImageScreen(
    callbacks: DetailedScreenCallbacks,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Detailed Image",
                leadingContent = {
                    IconButton(
                        iconResId = R.drawable.ic_back,
                        onClick = callbacks.onBackClick,
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        // TODO: Implement the detailed image screen UI here
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Detailed Image Screen",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun DetailedImageScreenPreview() {
    DetailedImageScreen(
        callbacks = DetailedScreenCallbacks(
            onBackClick = {},
        )
    )
}

data class DetailedScreenCallbacks(
    val onBackClick: () -> Unit,
)
