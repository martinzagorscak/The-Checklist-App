package com.example.thechecklistapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechecklistapp.device.ConnectivityStatus
import com.example.thechecklistapp.device.ConnectivityStatusPublisher
import com.example.thechecklistapp.domain.usecase.GetChecklistUseCase
import com.example.thechecklistapp.domain.usecase.RefetchChecklistUseCase
import com.example.thechecklistapp.ui.model.PresentableChecklistItem
import com.example.thechecklistapp.ui.model.toPresentableModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ChecklistViewState {
    object Loading : ChecklistViewState()
    sealed class Error : ChecklistViewState() {
        object ConnectivityError : Error()
        object DataRetrievingError : Error()
    }

    data class Loaded(val checklistItems: List<PresentableChecklistItem>) : ChecklistViewState()
}

abstract class ChecklistViewModel : ViewModel() {
    abstract fun checklistViewState(): Flow<ChecklistViewState>
    abstract fun refetchChecklist()
    abstract fun checkItem(responseSetId: Int, responseId: Int, isMultipleChoice: Boolean)
}

internal class ChecklistViewModelImpl(
    getChecklistUseCase: GetChecklistUseCase,
    private val refetchChecklistUseCase: RefetchChecklistUseCase,
    private val connectivityStatusPublisher: ConnectivityStatusPublisher,
) : ChecklistViewModel() {

    // This map holds the checked items for each response set. <responseSetId, Set<responseIds>>
    private val checkedItems = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())

    private val checklistViewState: StateFlow<ChecklistViewState> = combine(
        getChecklistUseCase(),
        checkedItems,
        connectivityStatusPublisher.status(),
    ) { checklistItems, selectedItems, connectivityStatus ->
        when {
            connectivityStatus != ConnectivityStatus.CONNECTED -> ChecklistViewState.Error.ConnectivityError
            checklistItems == null -> ChecklistViewState.Error.DataRetrievingError
            checklistItems.isEmpty() -> ChecklistViewState.Loading
            else -> ChecklistViewState.Loaded(checklistItems.toPresentableModel(selectedItems))
        }
    }.catch {
        Log.e("ChecklistViewModelImpl", it.message ?: "Error occurred in the view state")
        emit(ChecklistViewState.Error.DataRetrievingError)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChecklistViewState.Loading
    )

    override fun checklistViewState(): Flow<ChecklistViewState> = checklistViewState

    override fun refetchChecklist() {
        viewModelScope.launch(Dispatchers.Default) {
            refetchChecklistUseCase()
        }
    }

    override fun checkItem(responseSetId: Int, responseId: Int, isMultipleChoice: Boolean) {
        checkedItems.update { current ->
            val mutableCurrent = current.toMutableMap()
            if (isMultipleChoice) {
                val selectedSet = mutableCurrent[responseSetId].orEmpty().toMutableSet()

                if (!selectedSet.add(responseId)) { // unselecting the item if it was already selected
                    selectedSet.remove(responseId)
                }

                if (selectedSet.isEmpty()) {
                    // remove the set entry if no items are selected
                    mutableCurrent.remove(responseSetId)
                } else {
                    // update the set with the new selection
                    mutableCurrent[responseSetId] = selectedSet
                }
            } else {
                // For single choice, just replacing any previous selection
                mutableCurrent[responseSetId] = setOf(responseId)
            }
            mutableCurrent
        }
    }
}
