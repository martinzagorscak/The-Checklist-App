package com.example.thechecklistapp.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class SharedElementTransitionScope(
    val sharedTransitionScope: SharedTransitionScope,
    val animatedVisibilityScope: AnimatedVisibilityScope,
)

fun checklistImageSharedElementKey(imageId: Int): String = "checklist-image-$imageId"

fun checklistImageTitleSharedElementKey(imageId: Int): String = "checklist-image-title-$imageId"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.applySharedTransition(
    scope: SharedElementTransitionScope?,
    key: String,
    shareOnlyBounds: Boolean = false,
): Modifier {
    return if (scope != null) {
        with(scope.sharedTransitionScope) {
            if (shareOnlyBounds) {
                sharedBounds(
                    sharedContentState = rememberSharedContentState(key = key),
                    animatedVisibilityScope = scope.animatedVisibilityScope,
                )
            } else {
                sharedElement(
                    sharedContentState = rememberSharedContentState(key = key),
                    animatedVisibilityScope = scope.animatedVisibilityScope,
                )
            }
        }
    } else {
        this
    }
}
