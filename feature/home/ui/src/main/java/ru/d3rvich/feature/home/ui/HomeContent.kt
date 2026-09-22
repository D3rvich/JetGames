package ru.d3rvich.feature.home.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ru.d3rvich.common.components.ScrollToTopButton
import ru.d3rvich.feature.home.api.HomeComponent
import ru.d3rvich.feature.home.ui.views.GamesGrid
import ru.d3rvich.feature.home.ui.views.HomeAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    component: HomeComponent,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val pagingItems = component.games.collectAsLazyPagingItems()
    val model by component.model.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isScrollingUp by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(gridState) {
        var previousFirstVisibleItemIndex = gridState.firstVisibleItemIndex
        var previousScrollOffset = gridState.firstVisibleItemScrollOffset
        snapshotFlow { gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset }
            .collect { (currentFirstVisibleItemIndex, currentScrollOffset) ->
                when {
                    previousFirstVisibleItemIndex != currentFirstVisibleItemIndex -> isScrollingUp =
                        previousFirstVisibleItemIndex > currentFirstVisibleItemIndex

                    previousScrollOffset != currentScrollOffset -> isScrollingUp =
                        previousScrollOffset > currentScrollOffset
                }
                previousFirstVisibleItemIndex = currentFirstVisibleItemIndex
                previousScrollOffset = currentScrollOffset
            }
    }
    val showFab by remember(isScrollingUp, gridState) {
        derivedStateOf {
            isScrollingUp && gridState.firstVisibleItemIndex > 0
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            val scope = rememberCoroutineScope()
            ScrollToTopButton(
                isVisible = showFab,
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
            ) {
                scope.launch {
                    gridState.animateScrollToItem(0)
                }
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            if (pagingItems.loadState.refresh !is LoadState.Error || model.search.isNotEmpty()) {
                HomeAppBar(
                    searchText = model.search,
                    onSearchChange = component::setSearch,
                    isFilterEdited = model.isFilterEdited,
                    currentListDisplayOption = model.listDisplayOption,
                    onListDisplayOptionChange = component::setListVDisplayOption,
                    scrollBehavior = scrollBehavior,
                    navigateToFilterScreen = component::openFilter,
                    navigateToSettingsScreen = component::openSettings,
                )
            }
        }
    ) { paddingValues ->
        GamesGrid(
            games = pagingItems,
            listDisplayOption = model.listDisplayOption,
            onGameClick = component::openGameDetail,
            modifier = modifier,
            gridState = gridState,
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 8.dp,
                bottom = contentPadding.calculateBottomPadding()
            )
        )
    }
}