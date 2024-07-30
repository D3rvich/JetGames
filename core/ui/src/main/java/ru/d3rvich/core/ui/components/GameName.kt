package ru.d3rvich.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.ui.icon.RatingType
import ru.d3rvich.core.ui.icon.textIcon
import ru.d3rvich.core.ui.theme.JetGamesTheme

/**
 * Created by Ilya Deryabin at 27.06.2024
 */
@Composable
fun GameName(
    modifier: Modifier = Modifier,
    name: String,
    ratingType: RatingType?,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    iconStyle: SpanStyle = style.toSpanStyle(),
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(name)
            ratingType?.let {
                withStyle(iconStyle) {
                    append(" ${ratingType.textIcon}")
                }
            }
        },
        maxLines = maxLines,
        style = style,
    )
}

@Preview(showBackground = true)
@Composable
private fun GameNamePreview() {
    JetGamesTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RatingType.entries.forEach {
                GameName(name = "Game name", ratingType = it)
            }
        }
    }
}