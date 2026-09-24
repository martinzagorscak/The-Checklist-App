package com.example.thechecklistapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.thechecklistapp.R
import com.example.thechecklistapp.ui.model.PresentableChecklistChoiceItem
import com.example.thechecklistapp.ui.model.PresentableChecklistImageItem
import com.example.thechecklistapp.ui.model.PresentableChecklistItem
import com.example.thechecklistapp.ui.model.PresentableChecklistPage
import com.example.thechecklistapp.ui.model.PresentableChecklistSection
import com.example.thechecklistapp.ui.model.PresentableChecklistTextItem
import com.example.thechecklistapp.ui.theme.Primary40
import com.example.thechecklistapp.ui.theme.Typography
import com.example.thechecklistapp.ui.theme.padding100
import com.example.thechecklistapp.ui.theme.padding200
import com.example.thechecklistapp.ui.theme.padding400

private val roundedCornerSize = 12.dp
private val minChecklistImageWidth = 100.dp
private val maxChecklistImageWidth = 200.dp
private val maxResponseSetHeight = 200.dp
private val borderWidth = 1.dp
private val labelScoreSize = 20.dp

@Composable
fun Checklist(
    checklistItems: List<PresentableChecklistItem>,
    checklistCallbacks: ChecklistCallbacks,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = true,
    sectionDepth: Int = 0,
) {
    if (isScrollable) { // Because of recursive callbacks when composing the checklist, LazyColumn is only for root level pages
        LazyColumn(modifier = modifier) {
            items(items = checklistItems, key = { checklistItemKey(it) }) { checklistItem ->
                ChecklistItem(
                    checklistItem = checklistItem,
                    checklistCallbacks = checklistCallbacks,
                    sectionDepth = sectionDepth,
                )
            }
        }
    } else {
        Column(modifier = modifier) {
            checklistItems.forEach { checklistItem ->
                ChecklistItem(
                    checklistItem = checklistItem,
                    checklistCallbacks = checklistCallbacks,
                    sectionDepth = sectionDepth,
                    modifier = Modifier.padding(vertical = padding100),
                )
            }
        }
    }
}

@Composable
private fun ChecklistItem(
    checklistItem: PresentableChecklistItem,
    checklistCallbacks: ChecklistCallbacks,
    sectionDepth: Int,
    modifier: Modifier = Modifier,
) {
    when (checklistItem) {
        is PresentableChecklistPage -> {
            Page(
                page = checklistItem,
                checklistCallbacks = checklistCallbacks,
                sectionDepth = sectionDepth,
                modifier = modifier,
            )
        }

        is PresentableChecklistSection -> {
            Section(
                section = checklistItem,
                checklistCallbacks = checklistCallbacks,
                sectionDepth = sectionDepth,
                modifier = modifier,
            )
        }

        is PresentableChecklistTextItem -> {
            TextItem(
                textItem = checklistItem,
                modifier = modifier,
            )
        }

        is PresentableChecklistImageItem -> {
            ImageItem(
                imageItem = checklistItem,
                onClick = { checklistCallbacks.onImageClick(checklistItem.id) },
                modifier = modifier,
            )
        }

        is PresentableChecklistChoiceItem -> {
            ChoiceItem(
                choiceItem = checklistItem,
                onChoiceClick = checklistCallbacks.onSelectableOptionClick,
            )
        }
    }
}

@Composable
private fun Page(
    page: PresentableChecklistPage,
    checklistCallbacks: ChecklistCallbacks,
    sectionDepth: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = padding200, horizontal = padding400)
            .fillMaxWidth()
            .clip(RoundedCornerShape(roundedCornerSize))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(padding400)
    ) {
        Text(
            text = page.title,
            style = Typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = padding100)
        )
        Checklist(
            checklistItems = page.items,
            checklistCallbacks = checklistCallbacks,
            modifier = Modifier.padding(start = padding200),
            isScrollable = false,
            sectionDepth = sectionDepth,
        )

    }
}

@Composable
private fun Section(
    section: PresentableChecklistSection,
    checklistCallbacks: ChecklistCallbacks,
    sectionDepth: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = section.title,
            style = sectionTitleStyle(sectionDepth).copy(fontWeight = Typography.bodyMedium.fontWeight),
        )
        Checklist(
            checklistItems = section.items,
            checklistCallbacks = checklistCallbacks,
            modifier = Modifier.padding(start = padding200),
            isScrollable = false,
            sectionDepth = sectionDepth + 1,
        )
    }
}

@Composable
private fun TextItem(
    textItem: PresentableChecklistTextItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = textItem.content,
            style = Typography.bodySmall,
        )
    }
}

@Composable
private fun ImageItem(
    imageItem: PresentableChecklistImageItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(roundedCornerSize))
            .clickable(onClick = onClick)
            .border(
                width = borderWidth,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(roundedCornerSize)
            )
            .padding(padding200)
    ) {
        AsyncImage(
            model = imageItem.src,
            contentDescription = imageItem.title,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.ic_photo_placeholder),
            fallback = painterResource(R.drawable.ic_photo_placeholder),
            modifier = Modifier.sizeIn(minWidth = minChecklistImageWidth, maxWidth = maxChecklistImageWidth),
        )
        Text(
            text = imageItem.title,
            style = Typography.bodySmall,
            modifier = Modifier.wrapContentWidth(),
        )
    }
}

@Composable
private fun ChoiceItem(
    choiceItem: PresentableChecklistChoiceItem,
    onChoiceClick: (responseSetId: Int, responseId: Int, isMultipleChoice: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = choiceItem.content,
            style = Typography.bodySmall,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(padding100),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxResponseSetHeight)
                .padding(vertical = padding100),
        ) {
            items(items = choiceItem.responseSet.responses, key = { "response-${it.id}" }) { response ->
                val backgroundColor = if (response.isChecked) Primary40 else MaterialTheme.colorScheme.tertiary
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(roundedCornerSize))
                        .background(backgroundColor)
                        .clickable(onClick = {
                            onChoiceClick(
                                choiceItem.responseSet.id,
                                response.id,
                                choiceItem.responseSet.multipleSelection
                            )
                        })
                        .padding(padding200)
                ) {
                    Text(
                        text = response.label,
                        style = Typography.bodySmall,
                    )
                    response.score?.let {
                        Spacer(modifier = Modifier.width(padding100))
                        Text(
                            text = it.toString(),
                            style = Typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .size(labelScoreSize)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            }
        }
    }
}

private fun checklistItemKey(item: PresentableChecklistItem): String =
    when (item) {
        is PresentableChecklistPage -> "page-${item.id}"
        is PresentableChecklistSection -> "section-${item.id}"
        is PresentableChecklistTextItem -> "text-${item.id}"
        is PresentableChecklistImageItem -> "image-${item.id}"
        is PresentableChecklistChoiceItem -> "choice-${item.id}"
    }

private fun sectionTitleStyle(sectionDepth: Int) =
    if (sectionDepth == 0) Typography.bodyMedium else Typography.bodySmall

data class ChecklistCallbacks(
    val onImageClick: (imageItemId: Int) -> Unit,
    val onSelectableOptionClick: (responseSetId: Int, responseId: Int, isMultipleChoice: Boolean) -> Unit,
)
