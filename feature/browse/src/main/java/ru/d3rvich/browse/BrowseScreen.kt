package ru.d3rvich.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import ru.d3rvich.browse.model.BrowseUiState
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.entities.GenreFullEntity

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@Composable
fun BrowseScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    imageLoader: ImageLoader,
) {
    val viewModel: BrowseViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BrowseScreen(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
        imageLoader = imageLoader
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BrowseScreen(
    modifier: Modifier = Modifier,
    state: BrowseUiState,
    contentPadding: PaddingValues,
    imageLoader: ImageLoader,
) {
    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.browse)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(contentPadding),
        ) {
            GenresView(status = state.genres, imageLoader = imageLoader)
        }
    }
}

@Composable
internal fun GenresView(
    modifier: Modifier = Modifier,
    status: Status<List<GenreFullEntity>>,
    imageLoader: ImageLoader,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.genres),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {
            when (status) {
                is Status.Error -> {
                    Text(text = "Error", modifier = Modifier.fillMaxSize())
                }

                Status.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is Status.Success -> {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(
                            items = status.value.sortedByDescending { it.gamesCount },
                            key = { it.id }) { genre ->
                            GenresItemView(genre = genre, imageLoader = imageLoader)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GenresItemView(
    modifier: Modifier = Modifier,
    genre: GenreFullEntity,
    imageLoader: ImageLoader,
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .width(200.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = genre.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = genre.name)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Text(text = "Games")
                    Text(text = genre.gamesCount.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenresItemViewPreview() {
    JetGamesTheme {
        Box(modifier = Modifier.size(200.dp)) {
            GenresItemView(
                genre = GenreFullEntity(
                    id = 0,
                    name = "Genre",
                    imageUrl = null,
                    gamesCount = 20
                ), imageLoader = LocalContext.current.imageLoader
            )
        }
    }
}