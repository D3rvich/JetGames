package ru.d3rvich.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import ru.d3rvich.common.components.raitingbar.RatingStar
import ru.d3rvich.common.components.shimmer.shimmer
import ru.d3rvich.core.ui.icon.findWrapper
import ru.d3rvich.core.ui.model.GameUiModel
import ru.d3rvich.core.ui.theme.JetGamesTheme

@Composable
fun GameGridItemView(
    modifier: Modifier = Modifier,
    game: GameUiModel?,
    isLoading: Boolean = false,
    imageLoader: ImageLoader,
    onItemClick: ((gameId: Int) -> Unit)? = null,
) {
    if (isLoading) {
        GameGridItemLoadingView(modifier = modifier)
    } else {
        requireNotNull(game)
        requireNotNull(onItemClick)
        GameGridItemView(
            modifier = modifier,
            game = game,
            imageLoader = imageLoader,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun GameGridItemLoadingView(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(0.dp).widthIn(max = 200.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmer()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .height(24.dp)
                    .width(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmer()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.6f)
                .shimmer()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(16.dp)
                .width(36.dp)
                .shimmer()
        )
    }
}

@Composable
private fun GameGridItemView(
    modifier: Modifier = Modifier,
    game: GameUiModel,
    imageLoader: ImageLoader,
    onItemClick: (gameId: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .widthIn(max = 200.dp)
            .clickable { onItemClick(game.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            AsyncImage(
                modifier = modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                model = game.imageUrl,
                contentDescription = game.name,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop
            )
            game.metacritic?.let { metacriticNotNull ->
                MetacriticScore(
                    metacriticScore = metacriticNotNull,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                    fillBackground = true,
                    textColor = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        GameName(
            name = game.name,
            ratingType = game.ratings?.maxByOrNull { it.percent }?.findWrapper(),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        if (game.rating != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "%1.1f".format(game.rating),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                RatingStar(size = 12.dp)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun GameGridItemLoadingViewPreview() {
    JetGamesTheme {
        Box(
            modifier = Modifier
                .height(250.dp)
                .width(400.dp), contentAlignment = Alignment.Center
        ) {
            GameGridItemLoadingView()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameGridItemViewPreview() {
    JetGamesTheme {
        Box(
            modifier = Modifier
                .height(250.dp)
                .width(400.dp), contentAlignment = Alignment.Center
        ) {
            val game = GameUiModel(
                id = 0,
                name = "name",
                imageUrl = null,
                metacritic = 50,
                rating = 2.5f,
                released = null,
                genres = null,
                parentPlatforms = null,
                ratings = null
            )
            GameGridItemView(game = game, imageLoader = LocalContext.current.imageLoader)
        }
    }
}