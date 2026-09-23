package com.example.thechecklistapp.data.repository

import com.example.thechecklistapp.data.api.ChecklistApi
import com.example.thechecklistapp.data.model.ApiChecklistItem
import com.example.thechecklistapp.domain.model.ChecklistItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ChecklistRepository {
    /**
     * Returns a [Flow] of the checklist items. The flow will emit the current list of checklist items.
     * The list of checklist items is initially empty and will be updated when the repository fetches the checklist items from the API or from cache.
     * The flow will emit null if there was an error fetching.
     */
    fun getChecklist(): Flow<List<ChecklistItem>?>

    /**
     * In case of an error, this function can be called to refetch the checklist items from the API.
     * The flow returned by [getChecklist] will emit the new list of checklist items when the refetch is complete.
     */
    suspend fun refetchChecklist()
}

internal class ChecklistRepositoryImpl(
    private val checklistApi: ChecklistApi,
    scope: CoroutineScope,
) : ChecklistRepository {

    private val checklistPublisher = MutableStateFlow<List<ChecklistItem>?>(emptyList())
    private val refetchChecklistPublisher = MutableSharedFlow<Unit>()

    init {
        scope.launch {
            refetchChecklistPublisher
                .onStart { emit(Unit) }
                .collect {
                    // TODO logic to fetch from cache first, then from API if cache is empty
                    // also store the checklist items in cache after fetching from API
                    checklistPublisher.update { fetchChecklistFromApi() }
                }
        }
    }

    override fun getChecklist(): Flow<List<ChecklistItem>?> = checklistPublisher

    override suspend fun refetchChecklist() = refetchChecklistPublisher.emit(Unit)

    private suspend fun fetchChecklistFromApi(): List<ChecklistItem>? {
        val checklistItemsResponse = checklistApi.getChecklistItems()
        val checklistItems = checklistItemsResponse.getOrNull()?.map(ApiChecklistItem::toDomain)
        return checklistItems
    }
}
