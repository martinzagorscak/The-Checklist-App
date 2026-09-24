package com.example.thechecklistapp.data.repository

import android.util.Log
import com.example.thechecklistapp.data.api.ChecklistApi
import com.example.thechecklistapp.data.model.ApiChecklistItem
import com.example.thechecklistapp.data.persistance.ChecklistLocalDataSource
import com.example.thechecklistapp.domain.model.ChecklistItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException

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
    private val checklistLocalDataSource: ChecklistLocalDataSource,
    scope: CoroutineScope,
) : ChecklistRepository {

    private val checklistPublisher = MutableStateFlow<List<ChecklistItem>?>(emptyList())
    private val refetchChecklistPublisher = MutableSharedFlow<Unit>()

    init {
        scope.launch {
            val cachedChecklist = loadCachedChecklist()
            if (cachedChecklist.isNotEmpty()) {
                checklistPublisher.value = cachedChecklist
            } else {
                checklistPublisher.value = fetchChecklistFromApi()
            }

            refetchChecklistPublisher.collect {
                val refreshedChecklist = fetchChecklistFromApi()
                if (refreshedChecklist != null) {
                    checklistPublisher.value = refreshedChecklist
                } else if (checklistPublisher.value.isNullOrEmpty()) {
                    checklistPublisher.value = loadCachedChecklist().ifEmpty { null }
                }
            }
        }
    }

    override fun getChecklist(): Flow<List<ChecklistItem>?> = checklistPublisher

    override suspend fun refetchChecklist() {
        checklistPublisher.emit(emptyList())
        refetchChecklistPublisher.emit(Unit)
    }

    private suspend fun fetchChecklistFromApi(): List<ChecklistItem>? {
        val apiChecklistItems = checklistApi.getChecklistItems().getOrNull() ?: return null
        checklistLocalDataSource.replaceChecklist(apiChecklistItems)
        return apiChecklistItems.map(ApiChecklistItem::toDomain)
    }

    private suspend fun loadCachedChecklist(): List<ChecklistItem> =
        try {
            checklistLocalDataSource.getChecklist()
        } catch (exception: SerializationException) {
            Log.e("ChecklistRepositoryImpl", "Cached checklist payload is invalid", exception)
            checklistLocalDataSource.clearChecklist()
            emptyList()
        }
}
