package com.example.thechecklistapp.data.api.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface ApiChecklistItem {
    val id: Int
}

@Serializable
@SerialName("page")
data class ApiPage(
    override val id: Int,
    val title: String,
    val items: List<ApiChecklistItem> = emptyList()
) : ApiChecklistItem

@Serializable
@SerialName("section")
data class ApiSection(
    override val id: Int,
    val title: String,
    val items: List<ApiChecklistItem> = emptyList()
) : ApiChecklistItem

@Serializable
@SerialName("text")
data class ApiTextItem(
    override val id: Int,
    val content: String
) : ApiChecklistItem

@Serializable
@SerialName("image")
data class ApiImageItem(
    override val id: Int,
    val title: String,
    val src: String
) : ApiChecklistItem

@Serializable
@SerialName("choice")
data class ApiChoiceItem(
    override val id: Int,
    val content: String,
    @SerialName("response_set")
    val responseSet: ApiResponseSet
) : ApiChecklistItem

@Serializable
data class ApiResponseSet(
    val id: Int,
    @SerialName("multiple_selection")
    val multipleSelection: Boolean,
    val responses: List<ApiResponse>
)

@Serializable
data class ApiResponse(
    val id: Int,
    val label: String,
    val score: Int? = null
)
