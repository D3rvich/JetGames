package ru.d3rvich.feature.home.ui

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
import ru.d3rvich.common.components.ScrollToTopButton
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.feature.home.api.HomeComponent
import ru.d3rvich.feature.home.ui.views.GamesGrid
import ru.d3rvich.feature.home.ui.views.HomeAppBar
import kotlin.math.roundToInt

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
    BoxWithConstraints {
        val cellMinWidth = remember(model.listDisplayOption) {
            if (model.listDisplayOption == ListDisplayOption.Grid) 160.dp
            else 300.dp
        }
        val maxItemsInColumn = (this.maxWidth / cellMinWidth).roundToInt()
        val isButtonVisible by remember(maxItemsInColumn) {
            derivedStateOf {
                gridState.firstVisibleItemIndex > maxItemsInColumn * 3
            }
        }
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                val scope = rememberCoroutineScope()
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
                cellMinWidth = cellMinWidth,
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
}