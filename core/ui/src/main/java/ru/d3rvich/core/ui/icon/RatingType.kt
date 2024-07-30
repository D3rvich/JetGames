package ru.d3rvich.core.ui.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.domain.entities.RatingEntity
import ru.d3rvich.core.ui.theme.JetGamesTheme

/**
 * Created by Ilya Deryabin at 27.06.2024
 */
@Immutable
enum class RatingType(val originalId: Int) {
    Exceptional(5),
    Recommended(4),
    Meh(3),
    Skip(1)
}

fun RatingEntity.findWrapper(): RatingType = when (id) {
    RatingType.Exceptional.originalId -> RatingType.Exceptional
    RatingType.Recommended.originalId -> RatingType.Recommended
    RatingType.Meh.originalId -> RatingType.Meh
    RatingType.Skip.originalId -> RatingType.Skip
    else -> throw IllegalArgumentException("$this doesn't compare with any of ${RatingType::class.qualifiedName}")
}

@Stable
val RatingType.textIcon: String
    get() = when (this) {
        RatingType.Exceptional -> "\uD83C\uDFAF"
        RatingType.Recommended -> "\uD83D\uDC4D"
        RatingType.Meh -> "\uD83D\uDE11"
        RatingType.Skip -> "\u26D4"
    }

@Preview(showBackground = true)
@Composable
private fun RatingTextIconPreview() {
    JetGamesTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RatingType.entries.forEach { rating ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = rating.textIcon, style = MaterialTheme.typography.displaySmall)
                    Text(text = rating.name)
                }
            }
        }
    }
}