package ru.d3rvich.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import kotlinx.coroutines.launch
import ru.d3rvich.common.components.ScrollToTopButton
import ru.d3rvich.home.model.HomeUiEvent
import ru.d3rvich.home.model.HomeUiState
import ru.d3rvich.home.model.ListViewMode
import ru.d3rvich.home.model.rememberListViewModeProvider
import ru.d3rvich.home.views.GamesView
import ru.d3rvich.home.views.HomeAppBar
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    contentPadding: PaddingValues,
    navigateToDetailScreen: (Int) -> Unit,
    navigateToFilterScreen: () -> Unit,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier,
        state = state,
        imageLoader = imageLoader,
        contentPadding = contentPadding,
        onSearchChange = { homeViewModel.obtainEvent(HomeUiEvent.OnSearchChange(it)) },
        onRefresh = { homeViewModel.obtainEvent(HomeUiEvent.OnRefresh) },
        navigateToDetailScreen = navigateToDetailScreen,
        navigateToFilterScreen = navigateToFilterScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    imageLoader: ImageLoader,
    contentPadding: PaddingValues,
    onSearchChange: (String) -> Unit,
    onRefresh: () -> Unit,
    navigateToDetailScreen: (Int) -> Unit,
    navigateToFilterScreen: () -> Unit,
) {
    val pagingItems = state.games.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
    val listViewModeProvider = rememberListViewModeProvider()
    val currentListViewMode by
    listViewModeProvider.currentListViewMode.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    BoxWithConstraints {
        val itemWidth = when (currentListViewMode) {
            ListViewMode.Grid -> 160.dp
            else -> 300.dp
        }
        val maxItemsInColumn = (maxWidth / itemWidth).roundToInt()
        val isButtonVisible by remember {
            derivedStateOf {
                gridState.firstVisibleItemIndex > maxItemsInColumn * 3
            }
        }
        Scaffold(modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                ScrollToTopButton(
                    isVisible = isButtonVisible,
                    modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
                ) {
                    scope.launch {
                        gridState.animateScrollToItem(0)
                    }
                }
            },
            contentWindowInsets = WindowInsets.statusBars,
            topBar = {
                if (pagingItems.loadState.refresh !is LoadState.Error || state.search.isNotEmpty()) {
                    HomeAppBar(
                        searchText = state.search,
                        onSearchChange = onSearchChange,
                        isFilterEdited = state.isFilterEdited,
                        currentListViewMode = currentListViewMode,
                        onListViewModeChange = listViewModeProvider::setListViewMode,
                        scrollBehavior = scrollBehavior,
                        navigateToFilterScreen = navigateToFilterScreen
                    )
                }
            }
        ) { paddingValues ->
            GamesView(
                pagingItems = pagingItems,
                imageLoader = imageLoader,
                listViewMode = currentListViewMode,
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    bottom = contentPadding.calculateBottomPadding()
                ),
                gridState = gridState,
                onRefreshPressed = onRefresh,
                onGameSelected = { gameId ->
                    navigateToDetailScreen(gameId)
                }
            )
        }
    }
}