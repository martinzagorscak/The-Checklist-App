package com.example.thechecklistapp.ui.model

import com.example.thechecklistapp.domain.model.ChecklistChoiceItem
import com.example.thechecklistapp.domain.model.ChecklistImageItem
import com.example.thechecklistapp.domain.model.ChecklistItem
import com.example.thechecklistapp.domain.model.ChecklistPage
import com.example.thechecklistapp.domain.model.ChecklistResponse
import com.example.thechecklistapp.domain.model.ChecklistResponseSet
import com.example.thechecklistapp.domain.model.ChecklistSection
import com.example.thechecklistapp.domain.model.ChecklistTextItem

fun List<ChecklistItem>.toPresentableModel(
    checkedItems: Map<Int, Set<Int>>,
): List<PresentableChecklistItem> = map { it.toPresentableModel(checkedItems) }

private fun ChecklistItem.toPresentableModel(
    checkedItems: Map<Int, Set<Int>>,
): PresentableChecklistItem =
    when (this) {
        is ChecklistPage -> PresentableChecklistPage(
            id = id,
            title = title,
            items = items.toPresentableModel(checkedItems),
        )

        is ChecklistSection -> PresentableChecklistSection(
            id = id,
            title = title,
            items = items.toPresentableModel(checkedItems),
        )

        is ChecklistTextItem -> PresentableChecklistTextItem(
            id = id,
            content = content,
        )

        is ChecklistImageItem -> PresentableChecklistImageItem(
            id = id,
            title = title,
            src = src,
        )

        is ChecklistChoiceItem -> PresentableChecklistChoiceItem(
            id = id,
            content = content,
            responseSet = responseSet.toPresentableModel(checkedItems),
        )
    }

private fun ChecklistResponseSet.toPresentableModel(
    checkedItems: Map<Int, Set<Int>>,
): PresentableChecklistResponseSet {
    val selectedResponseIds = checkedItems[id].orEmpty()
    return PresentableChecklistResponseSet(
        id = id,
        multipleSelection = multipleSelection,
        responses = responses.map { it.toPresentableModel(selectedResponseIds) },
    )
}

private fun ChecklistResponse.toPresentableModel(
    selectedResponseIds: Set<Int>,
): PresentableChecklistResponse =
    PresentableChecklistResponse(
        id = id,
        label = label,
        score = score,
        isChecked = selectedResponseIds.contains(id),
    )
