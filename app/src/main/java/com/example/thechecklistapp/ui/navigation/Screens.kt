package com.example.thechecklistapp.ui.navigation

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thechecklistapp.ui.screens.DetailedImageScreen
import com.example.thechecklistapp.ui.screens.DetailedScreenCallbacks
import com.example.thechecklistapp.ui.screens.MainChecklistScreen
import com.example.thechecklistapp.ui.screens.MainChecklistScreenCallbacks
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object MainChecklistScreen : Screen()

    @Serializable
    data class DetailedImageScreen(val imageSectionId: Int) : Screen()
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainChecklistScreen,
        enterTransition = { expandHorizontally() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { shrinkHorizontally() },
        modifier = modifier,
    ) {
        composable<Screen.MainChecklistScreen> {
            MainChecklistScreen(
                callbacks = remember {
                    MainChecklistScreenCallbacks(
                        onImageClick = { imageSectionId ->
                            navController.navigate(route = Screen.DetailedImageScreen(imageSectionId))
                        },
                        onSelectableOptionClick = { responseSetId, responseId ->
                            // TODO Handle selectable option click
                        }
                    )
                }
            )
        }
        composable<Screen.DetailedImageScreen> {
            DetailedImageScreen(
                callbacks = remember {
                    DetailedScreenCallbacks(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            )
        }
    }
}
