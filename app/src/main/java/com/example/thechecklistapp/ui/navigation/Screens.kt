package com.example.thechecklistapp.ui.navigation

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.thechecklistapp.ui.components.SharedElementTransitionScope
import com.example.thechecklistapp.ui.screens.DetailedImageScreen
import com.example.thechecklistapp.ui.screens.DetailedScreenCallbacks
import com.example.thechecklistapp.ui.screens.MainChecklistScreen
import com.example.thechecklistapp.ui.screens.MainChecklistScreenCallbacks
import com.example.thechecklistapp.ui.viewmodel.ChecklistViewModel
import com.example.thechecklistapp.ui.viewmodel.ChecklistViewState
import com.example.thechecklistapp.ui.viewmodel.DetailedImageViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
sealed class Screen {

    @Serializable
    data object MainChecklistScreen : Screen()

    @Serializable
    data class DetailedImageScreen(val imageSectionId: Int) : Screen()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    SharedTransitionLayout(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = Screen.MainChecklistScreen,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            composable<Screen.MainChecklistScreen> {
                val viewModel = koinViewModel<ChecklistViewModel>()
                val checklistViewState by viewModel.checklistViewState().collectAsState(initial = ChecklistViewState.Loading)
                val sharedElementTransitionScope = SharedElementTransitionScope(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )

                MainChecklistScreen(
                    checklistViewState = checklistViewState,
                    callbacks = remember {
                        MainChecklistScreenCallbacks(
                            onImageClick = { imageSectionId ->
                                navController.navigate(route = Screen.DetailedImageScreen(imageSectionId))
                            },
                            onSelectableOptionClick = { responseSetId, responseId, isMultipleChoice ->
                                viewModel.checkItem(
                                    responseSetId = responseSetId,
                                    responseId = responseId,
                                    isMultipleChoice = isMultipleChoice,
                                )
                            },
                            onRetryClick = viewModel::refetchChecklist,
                            onCheckConnectivityClick = {
                                context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                            },
                        )
                    },
                    sharedElementTransitionScope = sharedElementTransitionScope,
                )
            }
            composable<Screen.DetailedImageScreen> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.DetailedImageScreen>()
                val viewModel = koinViewModel<DetailedImageViewModel>(parameters = { parametersOf(route.imageSectionId) })
                val imageViewState by viewModel.imageViewState().collectAsState()
                val sharedElementTransitionScope = SharedElementTransitionScope(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )

                DetailedImageScreen(
                    imageId = route.imageSectionId,
                    imageViewState = imageViewState,
                    callbacks = remember {
                        DetailedScreenCallbacks(
                            onBackClick = { navController.popBackStack() }
                        )
                    },
                    sharedElementTransitionScope = sharedElementTransitionScope,
                )
            }
        }
    }
}
