package ru.d3rvich.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.datetime.LocalDate
import ru.d3rvich.common.components.raitingbar.RatingBar
import ru.d3rvich.common.components.shimmer.shimmer
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.domain.entities.GenreEntity
import ru.d3rvich.core.domain.entities.ParentPlatformEntity
import ru.d3rvich.core.ui.R
import ru.d3rvich.core.ui.icon.findWrapper
import ru.d3rvich.core.ui.icon.tryFindIcon
import ru.d3rvich.core.ui.mapper.toGameUiModel
import ru.d3rvich.core.ui.model.GameUiModel
import ru.d3rvich.core.ui.theme.JetGamesTheme

@Composable
fun GameListItemView(
    modifier: Modifier = Modifier,
    game: GameUiModel?,
    isLarge: Boolean,
    isLoading: Boolean = game == null,
    onItemClick: ((gameId: Int) -> Unit)? = null,
) {
    if (isLoading) {
        GameListItemLoadingView(modifier = modifier, isLarge = isLarge)
    } else {
        requireNotNull(game)
        requireNotNull(onItemClick)
        GameListItemView(
            modifier = modifier,
            game = game,
            isLarge = isLarge,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun GameListItemLoadingView(modifier: Modifier = Modifier, isLarge: Boolean) {
    if (isLarge) {
        Card(modifier = modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .shimmer()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(80.dp)
                        .shimmer()
                )
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmer()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.6f)
                    .height(32.dp)
                    .shimmer()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .width(100.dp)
                                .shimmer()
                        )
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .width(80.dp)
                                .shimmer()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    } else {
        Card(modifier = modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.End)
                            .height(24.dp)
                            .width(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmer()
                    )
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(0.7f)
                            .shimmer()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .width(80.dp)
                                .shimmer()
                        )
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .width(64.dp)
                                .shimmer()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameListItemView(
    modifier: Modifier = Modifier,
    game: GameUiModel,
    isLarge: Boolean,
    onItemClick: (gameId: Int) -> Unit,
) {
    if (isLarge) {
        LargeGameListItemView(
            modifier = modifier,
            game = game,
            onItemClick = onItemClick
        )
    } else {
        CompactGameListItemView(
            modifier = modifier,
            game = game,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun CompactGameListItemView(
    modifier: Modifier = Modifier,
    game: GameUiModel,
    onItemClick: (gameId: Int) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onItemClick(game.id) }
    ) {
        val imageSize = 140.dp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                contentScale = ContentScale.Crop,
                model = game.imageUrl,
                contentDescription = "Image of ${game.name}",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = imageSize),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp, end = 4.dp)
                        .height(28.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    game.metacritic?.let { metacriticNotNull ->
                        MetacriticScore(metacriticScore = metacriticNotNull)
                    }
                }
                GameName(
                    name = game.name,
                    ratingType = game.ratings?.maxByOrNull { it.percent }?.findWrapper(),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .padding(vertical = 4.dp)
                        .padding(end = 8.dp)
                ) {
                    game.rating?.let { ratingNotNull ->
                        RatingBar(
                            value = ratingNotNull, spaceBetween = 0.dp,
                            ratingStarSize = 16.dp,
                        )
                    }
                    game.released?.let { releasedNotNull ->
                        Text(
                            text = releasedNotNull.toString(),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LargeGameListItemView(
    modifier: Modifier = Modifier,
    game: GameUiModel,
    onItemClick: ((gameId: Int) -> Unit),
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onItemClick(game.id) }) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            model = game.imageUrl,
            contentDescription = game.name,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                game.parentPlatforms?.forEach { item ->
                    val painter = item.tryFindIcon()
                    if (painter != null) {
                        Icon(
                            painter = painter,
                            contentDescription = item.name,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            game.metacritic?.let { metacriticNotNull ->
                MetacriticScore(metacriticScore = metacriticNotNull)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        GameName(
            name = game.name,
            ratingType = game.ratings?.maxByOrNull { it.percent }?.findWrapper(),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            game.released?.let { releaseDate ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.release_date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = releaseDate.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            game.genres?.let { genres ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.genres),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 40.dp)
                    )
                    FlowRow(horizontalArrangement = Arrangement.End) {
                        genres.forEach { genre ->
                            Text(
                                text = genre.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            game.rating?.let { ratingNotNull ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.rating),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    RatingBar(value = ratingNotNull, ratingStarSize = 16.dp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LargeGameItemLoadingPreview() {
    JetGamesTheme {
        GameListItemLoadingView(isLarge = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun LargeGameItemPreview() {
    val parentPlatforms = listOf(
        ParentPlatformEntity(0, "PC"),
        ParentPlatformEntity(1, "Playstation"),
        ParentPlatformEntity(2, "XBOX")
    )
    val genres = listOf(
        GenreEntity(0, "Action"),
        GenreEntity(1, "Strategy"),
        GenreEntity(2, "RPG"),
    )
    val game = GameEntity(
        id = 0,
        name = "Game name",
        imageUrl = null,
        metacritic = 50,
        rating = 2.5f,
        released = LocalDate(1999, 12, 24),
        parentPlatforms = parentPlatforms,
        genres = genres,
        ratings = null
    )
    JetGamesTheme {
        GameListItemView(
            game = game.toGameUiModel(),
            isLarge = true,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactGameItemLoadingPreview() {
    JetGamesTheme {
        GameListItemLoadingView(isLarge = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactGameItemPreview() {
    val game = GameEntity(
        id = 0,
        name = "Game name",
        imageUrl = null,
        metacritic = 50,
        rating = 2.5f,
        released = LocalDate(1999, 12, 24),
        parentPlatforms = null,
        genres = null,
        ratings = null
    )
    JetGamesTheme {
        GameListItemView(
            game = game.toGameUiModel(),
            isLarge = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactGameItemWithLongNamePreview() {
    val game = GameEntity(
        id = 0,
        name = "A very very very very very very very long game name what overlaps text view",
        imageUrl = null,
        metacritic = 50,
        rating = 2.5f,
        released = LocalDate(2007, 11, 1),
        parentPlatforms = listOf(
            ParentPlatformEntity(0, "PC"),
            ParentPlatformEntity(1, "Xbox One")
        ),
        genres = null,
        ratings = null
    )
    JetGamesTheme {
        GameListItemView(
            game = game.toGameUiModel(),
            isLarge = false
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactGamesViewPreview() {
    JetGamesTheme {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(5) { itemNumber ->
                val game = GameEntity(
                    id = itemNumber,
                    name = "Game name $itemNumber",
                    imageUrl = null,
                    metacritic = 20 * (itemNumber + 1),
                    rating = itemNumber + 1.0f,
                    released = LocalDate(2007, 11, itemNumber + 1),
                    parentPlatforms = null,
                    genres = null,
                    ratings = null
                )
                GameListItemView(
                    game = game.toGameUiModel(),
                    isLarge = false,
                    onItemClick = {}
                )
            }
        }
    }
}
