package com.example.thechecklistapp.data.persistance.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_cache")
data class ChecklistCacheEntity(
    @PrimaryKey
    val id: String,
    val payload: String,
)
