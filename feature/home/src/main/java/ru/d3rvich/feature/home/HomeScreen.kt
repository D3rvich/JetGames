package ru.d3rvich.feature.home

import androidx.compose.foundation.layout.Box
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ru.d3rvich.common.components.ScrollToTopButton
import ru.d3rvich.feature.home.model.ListDisplayMode
import ru.d3rvich.feature.home.store.HomeStore
import ru.d3rvich.feature.home.views.GamesView
import ru.d3rvich.feature.home.views.HomeAppBar
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    navigateToDetailScreen: (Int) -> Unit,
    navigateToFilterScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    when (val state = homeViewModel.uiState.collectAsStateWithLifecycle().value) {
        HomeStore.State.Loading -> {
            Box(modifier.fillMaxSize())
        }

        is HomeStore.State.Content -> {
            HomeScreen(
                modifier = modifier,
                state = state,
                contentPadding = contentPadding,
                onIntent = homeViewModel::obtainIntent,
                navigateToDetailScreen = navigateToDetailScreen,
                navigateToFilterScreen = navigateToFilterScreen,
                navigateToSettingsScreen = navigateToSettingsScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    state: HomeStore.State.Content,
    contentPadding: PaddingValues,
    onIntent: (HomeStore.Intent) -> Unit,
    navigateToDetailScreen: (Int) -> Unit,
    navigateToFilterScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagingItems = state.games.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
    val currentListViewMode = state.listDisplayMode
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    BoxWithConstraints {
        val itemWidth = when (currentListViewMode) {
            ListDisplayMode.Grid -> 160.dp
            else -> 300.dp
        }
        val maxItemsInColumn = (this.maxWidth / itemWidth).roundToInt()
        val isButtonVisible by remember {
            derivedStateOf {
                gridState.firstVisibleItemIndex > maxItemsInColumn * 3
            }
        }
        Scaffold(
            modifier = modifier
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
                        onSearchChange = { onIntent(HomeStore.Intent.SearchChange(it)) },
                        isFilterEdited = state.isFilterEdited,
                        currentListDisplayMode = currentListViewMode,
                        onListDisplayModeChange = { onIntent(HomeStore.Intent.ListDisplayChange(it)) },
                        scrollBehavior = scrollBehavior,
                        navigateToFilterScreen = navigateToFilterScreen,
                        navigateToSettingsScreen = navigateToSettingsScreen,
                    )
                }
            }
        ) { paddingValues ->
            GamesView(
                pagingItems = pagingItems,
                listDisplayMode = currentListViewMode,
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    bottom = contentPadding.calculateBottomPadding()
                ),
                gridState = gridState,
                onRefreshPressed = { onIntent(HomeStore.Intent.Refresh) },
                onGameSelected = { gameId ->
                    navigateToDetailScreen(gameId)
                }
            )
        }
    }
}