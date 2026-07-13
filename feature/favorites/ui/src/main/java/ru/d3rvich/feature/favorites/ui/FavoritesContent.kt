package ru.d3rvich.feature.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.ui.components.GameListItemView
import ru.d3rvich.core.ui.mapper.toGameUiModel
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.common.R as commonR

@Composable
fun FavoritesContent(
    component: FavoritesComponent,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    FavoritesContent(
        model = component.model,
        contentPadding = contentPadding,
        onSettingsClicked = component::settingsClicked,
        onGameClicked = component::gameClicked,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesContent(
    model: FavoritesComponent.Model,
    contentPadding: PaddingValues,
    onSettingsClicked: () -> Unit,
    onGameClicked: (gameId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagingItems = model.games.collectAsLazyPagingItems()
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
                    IconButton(onClick = onSettingsClicked) {
                        Icon(
                            painter = painterResource(commonR.drawable.settings_24px),
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

            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }) { index ->
                pagingItems[index]?.let { item ->
                    GameListItemView(
                        game = item.toGameUiModel(),
                        isLarge = false,
                    ) { gameId ->
                        onGameClicked(gameId)
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