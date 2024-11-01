package ru.d3rvich.browse.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import ru.d3rvich.browse.R
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.model.Status

/**
 * Created by Ilya Deryabin at 29.10.2024
 */
@Composable
internal fun PlatformsView(
    modifier: Modifier = Modifier,
    platformsStatus: Status<List<PlatformEntity>>,
    imageLoader: ImageLoader,
) {
    SectionTemplateView(
        modifier = modifier,
        name = stringResource(id = R.string.platforms),
        status = platformsStatus
    ) { platforms ->
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                items = platforms.sortedByDescending { it.gamesCount },
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