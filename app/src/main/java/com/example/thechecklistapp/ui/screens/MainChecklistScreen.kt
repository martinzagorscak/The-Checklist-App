package com.example.thechecklistapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.thechecklistapp.R
import com.example.thechecklistapp.ui.components.AlignInTheMiddle
import com.example.thechecklistapp.ui.components.CTA
import com.example.thechecklistapp.ui.components.Checklist
import com.example.thechecklistapp.ui.components.ChecklistCallbacks
import com.example.thechecklistapp.ui.components.FallbackState
import com.example.thechecklistapp.ui.components.SharedElementTransitionScope
import com.example.thechecklistapp.ui.components.TopBar
import com.example.thechecklistapp.ui.viewmodel.ChecklistViewState

@Composable
fun MainChecklistScreen(
    checklistViewState: ChecklistViewState,
    callbacks: MainChecklistScreenCallbacks,
    modifier: Modifier = Modifier,
    sharedElementTransitionScope: SharedElementTransitionScope? = null,
) {
    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.checklist_screen_title))
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        when (checklistViewState) {
            is ChecklistViewState.Loaded -> {
                Checklist(
                    checklistItems = checklistViewState.checklistItems,
                    checklistCallbacks = remember {
                        ChecklistCallbacks(
                            onImageClick = callbacks.onImageClick,
                            onSelectableOptionClick = callbacks.onSelectableOptionClick,
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    sharedElementTransitionScope = sharedElementTransitionScope,
                )
            }

            ChecklistViewState.Error.ConnectivityError -> {
                AlignInTheMiddle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    FallbackState(
                        title = stringResource(R.string.unstable_network_connection_message),
                        cta = CTA(
                            label = stringResource(R.string.unstable_network_cta_label),
                            onClick = callbacks.onCheckConnectivityClick,
                        )
                    )
                }
            }

            ChecklistViewState.Error.DataRetrievingError -> {
                AlignInTheMiddle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    FallbackState(
                        title = stringResource(R.string.try_again),
                        cta = CTA(
                            label = stringResource(R.string.checklist_screen_error_message),
                            onClick = callbacks.onRetryClick,
                        )
                    )
                }
            }

            ChecklistViewState.Loading -> {
                AlignInTheMiddle(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainChecklistScreenPreview() {
    MainChecklistScreen(
        checklistViewState = ChecklistViewState.Loading,
        callbacks = MainChecklistScreenCallbacks(
            onImageClick = {},
            onSelectableOptionClick = { _, _, _ -> },
            onRetryClick = {},
            onCheckConnectivityClick = {},
        ),
    )
}

data class MainChecklistScreenCallbacks(
    val onImageClick: (id: Int) -> Unit,
    val onSelectableOptionClick: (responseSetId: Int, responseId: Int, isMultipleChoice: Boolean) -> Unit,
    val onRetryClick: () -> Unit,
    val onCheckConnectivityClick: () -> Unit,
)
