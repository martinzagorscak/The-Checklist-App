package com.example.thechecklistapp.data.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.thechecklistapp.data.persistance.model.ChecklistCacheEntity

@Dao
interface ChecklistCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ChecklistCacheEntity)

    @Query("SELECT * FROM checklist_cache WHERE id = :id")
    suspend fun getById(id: String): ChecklistCacheEntity?

    @Query("DELETE FROM checklist_cache WHERE id = :id")
    suspend fun deleteById(id: String)
}
