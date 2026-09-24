package com.example.thechecklistapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.thechecklistapp.ui.theme.Typography
import com.example.thechecklistapp.ui.theme.padding200
import com.example.thechecklistapp.ui.theme.padding800

data class CTA(
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun FallbackState(
    title: String,
    modifier: Modifier = Modifier,
    cta: CTA? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(padding800)
    ) {
        Text(
            text = title,
            style = Typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        cta?.let {
            Spacer(modifier = Modifier.height(padding200))
            Button(onClick = cta.onClick) {
                Text(
                    text = cta.label,
                    style = Typography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
