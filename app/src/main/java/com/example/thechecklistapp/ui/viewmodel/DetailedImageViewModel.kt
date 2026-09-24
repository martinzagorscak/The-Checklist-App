package com.example.thechecklistapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechecklistapp.domain.model.ChecklistImageItem
import com.example.thechecklistapp.domain.model.ChecklistItem
import com.example.thechecklistapp.domain.model.ChecklistPage
import com.example.thechecklistapp.domain.model.ChecklistSection
import com.example.thechecklistapp.domain.usecase.GetChecklistUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface DetailedImageViewState {
    data object Loading : DetailedImageViewState
    data object Error : DetailedImageViewState
    data object NotFound : DetailedImageViewState
    data class Loaded(
        val title: String,
        val src: String,
    ) : DetailedImageViewState
}

abstract class DetailedImageViewModel : ViewModel() {
    abstract fun imageViewState(): StateFlow<DetailedImageViewState>
}

internal class DetailedImageViewModelImpl(
    imageSectionId: Int,
    getChecklistUseCase: GetChecklistUseCase,
) : DetailedImageViewModel() {

    private val imageViewState: StateFlow<DetailedImageViewState> =
        getChecklistUseCase()
            .map { checklist ->
                when {
                    checklist == null -> DetailedImageViewState.Error
                    checklist.isEmpty() -> DetailedImageViewState.Loading
                    else -> checklist.findImage(imageSectionId)?.let { image ->
                        DetailedImageViewState.Loaded(
                            title = image.title,
                            src = image.src,
                        )
                    } ?: DetailedImageViewState.NotFound
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailedImageViewState.Loading,
            )

    override fun imageViewState(): StateFlow<DetailedImageViewState> = imageViewState
}

private fun List<ChecklistItem>.findImage(imageId: Int): ChecklistImageItem? {
    for (item in this) {
        val match = item.findImage(imageId)
        if (match != null) return match
    }
    return null
}

private fun ChecklistItem.findImage(imageId: Int): ChecklistImageItem? =
    when (this) {
        is ChecklistImageItem -> if (id == imageId) this else null
        is ChecklistPage -> items.findImage(imageId)
        is ChecklistSection -> items.findImage(imageId)
        else -> null
    }
