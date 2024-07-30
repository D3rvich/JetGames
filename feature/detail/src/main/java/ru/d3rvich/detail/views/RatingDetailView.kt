package ru.d3rvich.detail.views

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.d3rvich.core.domain.entities.RatingEntity
import ru.d3rvich.core.ui.icon.RatingType
import ru.d3rvich.core.ui.icon.findWrapper
import ru.d3rvich.core.ui.icon.textIcon
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.detail.R

/**
 * Created by Ilya Deryabin at 28.06.2024
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RatingDetailView(modifier: Modifier = Modifier, ratings: List<RatingEntity>) {
    val list = RatingType.entries
    val sortedRatings = remember {
        ratings.sortedBy {
            list.indexOf(it.findWrapper())
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            sortedRatings.forEach { rating ->
                val uiWrapper = remember { rating.findWrapper() }
                BoxWithConstraints(
                    modifier = Modifier
                        .height(40.dp)
                        .weight(rating.percent / 100)
                        .background(uiWrapper.verticalGradient),
                    contentAlignment = Alignment.BottomStart
                ) {
                    val density = LocalDensity.current
                    val maxWidthSp = with(density) {
                        maxWidth.toSp()
                    }
                    val textIconSize = 24.sp
                    if (maxWidthSp > textIconSize) {
                        Text(text = uiWrapper.textIcon, fontSize = textIconSize)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sortedRatings.forEach { rating ->
                val uiWrapper = remember { rating.findWrapper() }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(uiWrapper.verticalGradient)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(uiWrapper.textResId))
                    val color = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rating.count.toString(),
                        style = LocalTextStyle.current.copy(color = color)
                    )
                }
            }
        }
    }
}

private val RatingType.verticalGradient: Brush
    get() = when (this) {
        RatingType.Exceptional -> Brush.verticalGradient(
            listOf(
                Color(0XFFB4EC51),
                Color(0xFF429321)
            )
        )

        RatingType.Recommended -> Brush.verticalGradient(
            listOf(
                Color(0XFF649BFF),
                Color(0XFF4354B9)
            )
        )

        RatingType.Meh -> Brush.verticalGradient(
            listOf(
                Color(0XFFFAD961),
                Color(0XFFF76B1C)
            )
        )

        RatingType.Skip -> Brush.verticalGradient(
            listOf(
                Color(0XFFFF5764),
                Color(0XFFF11A2A)
            )
        )
    }

private val RatingType.textResId: Int
    get() = when (this) {
        RatingType.Exceptional -> R.string.rating_type_exceptional
        RatingType.Recommended -> R.string.rating_type_recommended
        RatingType.Meh -> R.string.rating_type_meh
        RatingType.Skip -> R.string.rating_type_skip
    }

@Preview(showBackground = true)
@Composable
private fun RatingDetailViewPreview() {
    val ratings = RatingType.entries.map { rating ->
        RatingEntity(
            id = rating.originalId,
            title = rating.name,
            count = 100,
            percent = 25f
        )
    }
    JetGamesTheme {
        RatingDetailView(ratings = ratings, modifier = Modifier.fillMaxWidth())
    }
}