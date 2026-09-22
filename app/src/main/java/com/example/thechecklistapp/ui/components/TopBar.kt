package com.example.thechecklistapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thechecklistapp.R
import com.example.thechecklistapp.ui.theme.Typography
import com.example.thechecklistapp.ui.theme.padding200
import com.example.thechecklistapp.ui.theme.padding300

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .windowInsetsPadding(insets = WindowInsets.statusBars)
            .fillMaxWidth()
            .padding(horizontal = padding200)
    ) {
        leadingContent?.invoke()

        Text(
            text = title,
            style = Typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = padding200,
                    vertical = padding300
                ),
        )

        trailingContent?.invoke()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TopBarPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(padding200)) {
        val iconButtonSize = 32.dp
        val leadingContent: @Composable () -> Unit = {
            IconButton(
                iconResId = R.drawable.ic_back,
                onClick = {},
                iconButtonSize = iconButtonSize,
            )
        }
        TopBar(title = "Header1")
        TopBar(
            title = "Header2",
            leadingContent = leadingContent,
        )
    }
}
