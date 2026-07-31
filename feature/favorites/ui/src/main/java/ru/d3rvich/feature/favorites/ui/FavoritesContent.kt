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
import androidx.paging.compose.collectAsLazyPagingItems
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.ui.components.GameListItemView
import ru.d3rvich.core.ui.model.GameUiModel
import ru.d3rvich.core.ui.paging.HandlePagingItems
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.common.R as commonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesContent(
    component: FavoritesComponent,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val games = component.games.collectAsLazyPagingItems()
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
                    IconButton(onClick = component::settingsClicked) {
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
        HandlePagingItems(
            items = games,
            onLoading = { LoadingView() },
            onEmpty = { NoItemsMessage() },
            onError = { error ->
                DefaultErrorView(
                    message = error.localizedMessage ?: "Error",
                    onRefreshPressed = { })
            }
        ) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = contentPadding
            ) {
                pagingItems(key = GameUiModel::id, contentType = { "GameItems" }) { game ->
                    GameListItemView(
                        game = game,
                        isLarge = false,
                        onItemClick = component::gameClicked,
                    )
                }
                appendLoadingItem {
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