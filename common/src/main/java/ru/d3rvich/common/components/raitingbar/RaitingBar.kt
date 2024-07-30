package ru.d3rvich.common.components.raitingbar

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    value: Float,
    @IntRange(from = 1) ratingStarsCount: Int = 5,
    ratingStarSize: Dp = RatingBarDefaults.RatingStarSize,
    strokeWidth: Dp = RatingBarDefaults.StrokeWidth,
    spaceBetween: Dp = RatingBarDefaults.SpaceBetween,
    colors: RatingBarColors = RatingBarDefaults.RatingBarColors,
) {
    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        (1..ratingStarsCount).forEach { step ->
            val stepRating = when {
                value > step -> 1f
                step.rem(value) < 1 -> value - (step - 1f)
                else -> 0f
            }
            RatingStar(
                value = stepRating,
                size = ratingStarSize,
                strokeWidth = strokeWidth,
                colors = colors
            )
        }
    }
}

@Stable
object RatingBarDefaults {
    val RatingStarSize = 24.dp
    val StrokeWidth = 1.dp
    val SpaceBetween = 4.dp

    val RatingBarColors: RatingBarColors
        @Composable get() = RatingBarColors(
            activeColor = MaterialTheme.colorScheme.primary,
            inActiveColor = Color.Transparent
        )
}

@Preview(showBackground = true)
@Composable
private fun RatingBarPreview() {
    MaterialTheme {
        RatingBar(value = 2.5f)
    }
}