package com.example.thechecklistapp.ui.model

sealed interface PresentableChecklistItem {
    val id: Int
}

data class PresentableChecklistPage(
    override val id: Int,
    val title: String,
    val items: List<PresentableChecklistItem> = emptyList(),
) : PresentableChecklistItem

data class PresentableChecklistSection(
    override val id: Int,
    val title: String,
    val items: List<PresentableChecklistItem> = emptyList(),
) : PresentableChecklistItem

data class PresentableChecklistTextItem(
    override val id: Int,
    val content: String,
) : PresentableChecklistItem

data class PresentableChecklistImageItem(
    override val id: Int,
    val title: String,
    val src: String,
) : PresentableChecklistItem

data class PresentableChecklistChoiceItem(
    override val id: Int,
    val content: String,
    val responseSet: PresentableChecklistResponseSet,
) : PresentableChecklistItem

data class PresentableChecklistResponseSet(
    val id: Int,
    val multipleSelection: Boolean,
    val responses: List<PresentableChecklistResponse>,
)

data class PresentableChecklistResponse(
    val id: Int,
    val label: String,
    val score: Int? = null,
    val isChecked: Boolean,
)
