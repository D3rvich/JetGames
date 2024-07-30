package ru.d3rvich.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.ui.theme.JetGamesTheme

/**
 * Created by Ilya Deryabin at 04.03.2024
 */
@Composable
fun MetacriticScore(
    modifier: Modifier = Modifier,
    metacriticScore: Int,
    fontSize: TextUnit = TextUnit.Unspecified,
    textColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    fillBackground: Boolean = false,
) {
    val color = when (metacriticScore) {
        in 0..49 -> Color.Red
        in 50..74 -> Color.Yellow
        in 75..100 -> Color.Green
        else -> error("A metacritic score must be in range [0..100]")
    }
    Text(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = color,
                    alpha = if (fillBackground) 1f else 0.5f,
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
            .border(width = 1.dp, color = color, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        textAlign = TextAlign.Center,
        text = metacriticScore.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = textColor,
        fontSize = fontSize
    )
}

@Preview(showBackground = true)
@Composable
private fun MetacriticPreview_Light() {
    JetGamesTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .width(200.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetacriticScore(metacriticScore = 30)
            MetacriticScore(metacriticScore = 74)
            MetacriticScore(metacriticScore = 81)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
private fun MetacriticScorePreview_Dark() {
    JetGamesTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier
                    .height(100.dp)
                    .width(200.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetacriticScore(metacriticScore = 30)
                MetacriticScore(metacriticScore = 74)
                MetacriticScore(metacriticScore = 81)
            }
        }
    }
}