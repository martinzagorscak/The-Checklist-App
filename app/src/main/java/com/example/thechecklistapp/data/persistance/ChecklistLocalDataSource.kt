package com.example.thechecklistapp.data.persistance

import com.example.thechecklistapp.data.model.ApiChecklistItem
import com.example.thechecklistapp.data.persistance.dao.ChecklistCacheDao
import com.example.thechecklistapp.data.persistance.model.ChecklistCacheEntity
import com.example.thechecklistapp.data.repository.toDomain
import com.example.thechecklistapp.domain.model.ChecklistItem
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private const val CACHE_ID = "checklist"

interface ChecklistLocalDataSource {
    suspend fun replaceChecklist(apiChecklistItems: List<ApiChecklistItem>)
    suspend fun getChecklist(): List<ChecklistItem>
    suspend fun clearChecklist()
}

internal class ChecklistLocalDataSourceImpl(
    private val checklistCacheDao: ChecklistCacheDao,
    private val json: Json,
) : ChecklistLocalDataSource {

    override suspend fun replaceChecklist(apiChecklistItems: List<ApiChecklistItem>) {
        checklistCacheDao.upsert(
            ChecklistCacheEntity(
                id = CACHE_ID,
                payload = json.encodeToString(
                    ListSerializer(ApiChecklistItem.serializer()),
                    apiChecklistItems,
                ),
            ),
        )
    }

    override suspend fun getChecklist(): List<ChecklistItem> {
        val cachedChecklist = checklistCacheDao.getById(CACHE_ID) ?: return emptyList()
        val apiChecklistItems = json.decodeFromString(
            ListSerializer(ApiChecklistItem.serializer()),
            cachedChecklist.payload,
        )
        return apiChecklistItems.map(ApiChecklistItem::toDomain)
    }

    override suspend fun clearChecklist() = checklistCacheDao.deleteById(CACHE_ID)
}
