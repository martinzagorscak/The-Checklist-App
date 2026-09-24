package com.example.thechecklistapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.thechecklistapp.R
import com.example.thechecklistapp.ui.components.AlignInTheMiddle
import com.example.thechecklistapp.ui.components.IconButton
import com.example.thechecklistapp.ui.components.TopBar
import com.example.thechecklistapp.ui.theme.Typography
import com.example.thechecklistapp.ui.theme.padding400
import com.example.thechecklistapp.ui.viewmodel.DetailedImageViewState

@Composable
fun DetailedImageScreen(
    imageViewState: DetailedImageViewState,
    callbacks: DetailedScreenCallbacks,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.detailed_image_screen_title),
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
        when (imageViewState) {
            DetailedImageViewState.Loading -> {
                AlignInTheMiddle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator()
                }
            }

            DetailedImageViewState.Error -> {
                AlignInTheMiddle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = stringResource(R.string.detailed_image_screen_error_message),
                        style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            DetailedImageViewState.NotFound -> {
                AlignInTheMiddle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = stringResource(R.string.detailed_image_screen_not_found_message),
                        style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            is DetailedImageViewState.Loaded -> Column(
                verticalArrangement = Arrangement.spacedBy(padding400),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = padding400),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = imageViewState.title,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                // TODO implement with Shared Element Transition
                AsyncImage(
                    model = imageViewState.src,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun DetailedImageScreenPreview() {
    DetailedImageScreen(
        imageViewState = DetailedImageViewState.Loading,
        callbacks = DetailedScreenCallbacks(
            onBackClick = {},
        )
    )
}

data class DetailedScreenCallbacks(
    val onBackClick: () -> Unit,
)
