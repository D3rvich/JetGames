package ru.d3rvich.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.ui.components.GameGridItemView
import ru.d3rvich.core.ui.components.GameListItemView
import ru.d3rvich.core.ui.mapper.toGameUiModel
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.home.R
import ru.d3rvich.home.model.ListViewMode

/**
 * Created by Ilya Deryabin at 27.02.2024
 */
@Composable
internal fun GamesView(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<GameEntity>,
    listViewMode: ListViewMode,
    contentPadding: PaddingValues = PaddingValues(),
    gridState: LazyGridState = rememberLazyGridState(),
    onRefreshPressed: () -> Unit,
    onGameSelected: (Int) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                focusManager.clearFocus()
                return super.onPreScroll(available, source)
            }
        }
    }
    when {
        pagingItems.loadState.refresh is LoadState.Error -> {
            val error = pagingItems.loadState.refresh as? LoadState.Error
            DefaultErrorView(
                message = error?.error?.message ?: "error",
                onRefreshPressed = onRefreshPressed
            )
        }

        pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.no_items))
            }
        }

        else -> {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(connection),
                columns = GridCells.Adaptive(if (listViewMode == ListViewMode.Grid) 160.dp else 300.dp),
                contentPadding = contentPadding + PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = gridState
            ) {
                if (pagingItems.loadState.refresh is LoadState.Loading) {
                    items(
                        count = if (listViewMode == ListViewMode.Grid) 40 else 20,
                        key = { "LoadingStub#$it" }) {
                        LaunchedEffect(Unit) {
                            gridState.scrollToItem(0)
                        }
                        if (listViewMode == ListViewMode.Grid) {
                            GameGridItemView(
                                game = null,
                                isLoading = true,
                            )
                        } else {
                            GameListItemView(
                                game = null,
                                isLoading = true,
                                isLarge = listViewMode == ListViewMode.Large,
                            )
                        }
                    }
                }

                items(count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }) { index ->
                    pagingItems[index]?.let { item ->
                        if (listViewMode == ListViewMode.Grid) {
                            GameGridItemView(
                                game = item.toGameUiModel(),
                                onItemClick = onGameSelected
                            )
                        } else {
                            GameListItemView(
                                game = item.toGameUiModel(),
                                isLarge = listViewMode == ListViewMode.Large,
                                onItemClick = onGameSelected
                            )
                        }
                    }
                }

                if (pagingItems.loadState.append == LoadState.Loading) {
                    items(
                        count = if (listViewMode == ListViewMode.Grid) 20 else 10,
                        key = { "LoadingStub#$it" }) {
                        if (listViewMode == ListViewMode.Grid) {
                            GameGridItemView(
                                game = null,
                                isLoading = true,
                            )
                        } else {
                            GameListItemView(
                                game = null,
                                isLoading = true,
                                isLarge = listViewMode == ListViewMode.Large,
                            )
                        }
                    }
                }
            }
        }
    }
}

private operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
            other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
            other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)

@Preview(showBackground = true)
@Composable
private fun GamesViewPreview() {
    JetGamesTheme {
        val pagingData = flowOf(
            PagingData.from(
                List(10) {
                    GameEntity(
                        id = it,
                        name = "Game $it",
                        imageUrl = null,
                        metacritic = (it + 1) * 10,
                        rating = (it + 1) % 5f,
                        ratings = null,
                        released = LocalDate(2007, 11, it + 1),
                        genres = null,
                        parentPlatforms = null
                    )
                }, sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(
                        false
                    ),
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false)
                )
            )
        )
        val games = pagingData.collectAsLazyPagingItems()
        GamesView(
            modifier = Modifier.fillMaxSize(),
            pagingItems = games,
            listViewMode = ListViewMode.Grid,
            onRefreshPressed = { },
            onGameSelected = {})
    }
}