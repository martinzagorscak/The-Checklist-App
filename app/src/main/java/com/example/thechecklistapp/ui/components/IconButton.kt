package com.example.thechecklistapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.thechecklistapp.R
import com.example.thechecklistapp.ui.theme.padding200

private val defaultIconButtonSize = 32.dp

@Composable
fun IconButton(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconButtonSize: Dp = defaultIconButtonSize,
    tint: Color = LocalContentColor.current,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(iconButtonSize)
                .padding(padding200),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun IconButtonPreview() {
    IconButton(
        iconResId = R.drawable.ic_back,
        onClick = {}
    )
}
