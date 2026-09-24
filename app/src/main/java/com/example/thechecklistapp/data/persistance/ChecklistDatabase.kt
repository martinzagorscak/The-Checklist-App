package com.example.thechecklistapp.data.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.thechecklistapp.data.persistance.dao.ChecklistCacheDao
import com.example.thechecklistapp.data.persistance.model.ChecklistCacheEntity

@Database(
    entities = [ChecklistCacheEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class ChecklistDatabase : RoomDatabase() {
    abstract fun checklistCacheDao(): ChecklistCacheDao
}