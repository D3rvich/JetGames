package ru.d3rvich.browse.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.imageLoader
import ru.d3rvich.browse.R
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.ui.theme.JetGamesTheme

/**
 * Created by Ilya Deryabin at 13.09.2024
 */
@Composable
internal fun GenresView(
    modifier: Modifier = Modifier,
    genresStatus: Status<List<GenreFullEntity>>,
    imageLoader: ImageLoader,
) {
    SectionTemplateView(
        modifier = modifier,
        name = stringResource(id = R.string.genres),
        status = genresStatus
    ) { genres ->
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                items = genres.sortedByDescending { it.gamesCount },
                key = { it.id }) { genre ->
                BrowseSectionItemView(
                    itemName = genre.name,
                    itemCount = genre.gamesCount,
                    itemImageUrl = genre.imageUrl,
                    imageLoader = imageLoader
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenresViewPreview() {
    JetGamesTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(5) { item ->
                val genre = GenreFullEntity(
                    id = item,
                    name = "Genre $item",
                    gamesCount = item * 20,
                    imageUrl = null
                )
                BrowseSectionItemView(
                    itemName = genre.name,
                    itemCount = genre.gamesCount,
                    itemImageUrl = genre.imageUrl,
                    imageLoader = LocalContext.current.imageLoader
                )
            }
        }
    }
}