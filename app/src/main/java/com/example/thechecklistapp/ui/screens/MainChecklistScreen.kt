package com.example.thechecklistapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainChecklistScreen(
    callbacks: MainChecklistScreenCallbacks,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        // TODO: Implement the main checklist screen UI here
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable(onClick = { callbacks.onImageClick(-1) })
        ) {
            Text(
                text = "Main Checklist Screen",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun MainChecklistScreenPreview() {
    MainChecklistScreen(
        callbacks = MainChecklistScreenCallbacks(
            onImageClick = {},
            onSelectableOptionClick = { _, _ -> }
        )
    )
}

data class MainChecklistScreenCallbacks(
    val onImageClick: (id: Int) -> Unit,
    val onSelectableOptionClick: (responseSetId: Int, responseId: Int) -> Unit,
)
