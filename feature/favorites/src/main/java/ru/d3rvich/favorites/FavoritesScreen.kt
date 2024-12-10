package ru.d3rvich.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.ImageLoader
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.ui.components.GameListItemView
import ru.d3rvich.core.ui.mapper.toGameUiModel
import ru.d3rvich.favorites.model.FavoritesUiState

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    imageLoader: ImageLoader,
    navigateToGameDetail: (gameId: Int) -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    val viewModel: FavoritesViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        modifier = modifier,
        state = state,
        imageLoader = imageLoader,
        contentPadding = contentPadding,
        navigateToGameDetail = navigateToGameDetail,
        navigateToSettingsScreen = navigateToSettingsScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    modifier: Modifier = Modifier,
    state: FavoritesUiState,
    imageLoader: ImageLoader,
    contentPadding: PaddingValues,
    navigateToGameDetail: (gameId: Int) -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    val pagingItems = state.games.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.favorites)) },
                actions = {
                    IconButton(onClick = navigateToSettingsScreen) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.open_settings)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (pagingItems.loadState.refresh == LoadState.Loading) {
                item {
                    LoadingView(modifier = Modifier.fillParentMaxSize())
                }
            }

            if (pagingItems.loadState.refresh is LoadState.Error) {
                item {
                    val error = pagingItems.loadState.refresh as? LoadState.Error
                    DefaultErrorView(
                        message = error?.error?.message ?: "error",
                        modifier = Modifier.fillParentMaxSize(),
                        onRefreshPressed = { }
                    )
                }
            }

            if (pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount == 0) {
                item {
                    NoItemsMessage(modifier = Modifier.fillParentMaxSize())
                }
            }

            items(count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }) { index ->
                pagingItems[index]?.let { item ->
                    GameListItemView(
                        game = item.toGameUiModel(),
                        isLarge = false,
                        imageLoader = imageLoader
                    ) { gameId ->
                        navigateToGameDetail(gameId)
                    }
                }
            }

            if (pagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
internal fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun NoItemsMessage(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.no_elements))
    }
}