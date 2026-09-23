package com.example.thechecklistapp.domain.model

sealed interface ChecklistItem {
    val id: Int
}

data class ChecklistPage(
    override val id: Int,
    val title: String,
    val items: List<ChecklistItem> = emptyList(),
) : ChecklistItem

data class ChecklistSection(
    override val id: Int,
    val title: String,
    val items: List<ChecklistItem> = emptyList(),
) : ChecklistItem

data class ChecklistTextItem(
    override val id: Int,
    val content: String,
) : ChecklistItem

data class ChecklistImageItem(
    override val id: Int,
    val title: String,
    val src: String,
) : ChecklistItem

data class ChecklistChoiceItem(
    override val id: Int,
    val content: String,
    val responseSet: ChecklistResponseSet,
) : ChecklistItem

data class ChecklistResponseSet(
    val id: Int,
    val multipleSelection: Boolean,
    val responses: List<ChecklistResponse>,
)

data class ChecklistResponse(
    val id: Int,
    val label: String,
    val score: Int? = null,
)
