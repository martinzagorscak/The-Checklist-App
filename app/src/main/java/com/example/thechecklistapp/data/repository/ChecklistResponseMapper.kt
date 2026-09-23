package com.example.thechecklistapp.data.repository

import com.example.thechecklistapp.data.model.ApiChecklistItem
import com.example.thechecklistapp.data.model.ApiChoiceItem
import com.example.thechecklistapp.data.model.ApiImageItem
import com.example.thechecklistapp.data.model.ApiPage
import com.example.thechecklistapp.data.model.ApiResponse
import com.example.thechecklistapp.data.model.ApiResponseSet
import com.example.thechecklistapp.data.model.ApiSection
import com.example.thechecklistapp.data.model.ApiTextItem
import com.example.thechecklistapp.domain.model.ChecklistChoiceItem
import com.example.thechecklistapp.domain.model.ChecklistImageItem
import com.example.thechecklistapp.domain.model.ChecklistItem
import com.example.thechecklistapp.domain.model.ChecklistPage
import com.example.thechecklistapp.domain.model.ChecklistResponse
import com.example.thechecklistapp.domain.model.ChecklistResponseSet
import com.example.thechecklistapp.domain.model.ChecklistSection
import com.example.thechecklistapp.domain.model.ChecklistTextItem

fun ApiChecklistItem.toDomain(): ChecklistItem =
    when (this) {
        is ApiPage -> toDomain()
        is ApiSection -> toDomain()
        is ApiTextItem -> toDomain()
        is ApiImageItem -> toDomain()
        is ApiChoiceItem -> toDomain()
    }

private fun ApiPage.toDomain(): ChecklistPage =
    ChecklistPage(
        id = id,
        title = title,
        items = items.map { it.toDomain() },
    )

private fun ApiSection.toDomain(): ChecklistSection =
    ChecklistSection(
        id = id,
        title = title,
        items = items.map { it.toDomain() },
    )

private fun ApiTextItem.toDomain(): ChecklistTextItem =
    ChecklistTextItem(
        id = id,
        content = content,
    )

private fun ApiImageItem.toDomain(): ChecklistImageItem =
    ChecklistImageItem(
        id = id,
        title = title,
        src = src,
    )

private fun ApiChoiceItem.toDomain(): ChecklistChoiceItem =
    ChecklistChoiceItem(
        id = id,
        content = content,
        responseSet = responseSet.toDomain(),
    )

private fun ApiResponseSet.toDomain(): ChecklistResponseSet =
    ChecklistResponseSet(
        id = id,
        multipleSelection = multipleSelection,
        responses = responses.map { it.toDomain() },
    )

private fun ApiResponse.toDomain(): ChecklistResponse =
    ChecklistResponse(
        id = id,
        label = label,
        score = score,
    )
