package ru.d3rvich.detail.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.d3rvich.core.ui.components.MetacriticScore
import ru.d3rvich.common.components.raitingbar.RatingBar
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.detail.R

/**
 * Created by Ilya Deryabin at 15.03.2024
 */
@Composable
internal fun RatingsView(modifier: Modifier = Modifier, metacritic: Int?, rating: Float?) {
    check(metacritic != null || rating != null)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        if (metacritic != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.metacritic),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                MetacriticScore(
                    metacriticScore = metacritic,
                    fontSize = 20.sp
                )
            }
        }
        if (metacritic != null && rating != null) {
            VerticalDivider()
        }
        if (rating != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.rating),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                RatingBar(value = rating, spaceBetween = 0.dp, ratingStarSize = 24.dp)
                Text(text = "${rating}/5", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingsViewPreview() {
    JetGamesTheme {
        RatingsView(metacritic = 50, rating = 3.5f)
    }
}