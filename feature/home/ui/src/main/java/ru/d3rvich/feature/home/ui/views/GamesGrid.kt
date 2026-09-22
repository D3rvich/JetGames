package ru.d3rvich.feature.home.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.ui.components.GameGridItemView
import ru.d3rvich.core.ui.components.GameListItemView
import ru.d3rvich.core.ui.model.GameUiModel
import ru.d3rvich.core.ui.paging.HandlePagingItems
import ru.d3rvich.feature.home.ui.R

@Composable
internal fun GamesGrid(
    games: LazyPagingItems<GameUiModel>,
    listDisplayOption: ListDisplayOption,
    cellMinWidth: Dp,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val focusManager = LocalFocusManager.current
    val connection = remember(focusManager) {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                focusManager.clearFocus()
                return super.onPreScroll(available, source)
            }
        }
    }
    HandlePagingItems(
        modifier = modifier,
        items = games,
        onLoading = {
            LoadingView()
        },
        onEmpty = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.no_items))
            }
        },
        onError = { error ->
            DefaultErrorView(
                message = error.localizedMessage ?: "Unknown error",
                onRefreshPressed = games::refresh
            )
        }) {
        val columns = remember(cellMinWidth) { GridCells.Adaptive(cellMinWidth) }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .nestedScroll(connection),
                columns = columns,
                state = gridState,
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                pagingItems(
                    key = GameUiModel::id,
                    contentType = { listDisplayOption }) { item ->
                    GameItemView(
                        game = item,
                        listDisplayOption = listDisplayOption,
                        onGameClick = onGameClick
                    )
                }
                appendLoadingItem {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            AnimatedVisibility(
                visible = loadState.refresh is LoadState.Loading,
                modifier = Modifier.padding(contentPadding)
            ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), gapSize = 8.dp)
            }
        }
    }
}

@Composable
private fun GameItemView(
    game: GameUiModel,
    listDisplayOption: ListDisplayOption,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (listDisplayOption == ListDisplayOption.Grid) {
        GameGridItemView(
            game = game,
            onItemClick = onGameClick,
            modifier = modifier
        )
    } else {
        GameListItemView(
            game = game,
            isLarge = listDisplayOption == ListDisplayOption.Large,
            onItemClick = onGameClick,
            modifier = modifier
        )
    }
}