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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import ru.d3rvich.common.components.ScrollToTopButton
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.feature.home.api.HomeComponent
import ru.d3rvich.feature.home.ui.views.GamesView
import ru.d3rvich.feature.home.ui.views.HomeAppBar
import ru.d3rvich.feature.home.ui.views.LoadingView
import kotlin.math.roundToInt

@Composable
fun HomeContent(
    component: HomeComponent,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()
    when (val model = model) {
        is HomeComponent.Model.Content -> HomeContent(
            model = model,
            contentPadding = contentPadding,
            navigateToDetailScreen = component::openGameDetail,
            navigateToFilterScreen = component::openFilter,
            navigateToSettingsScreen = component::openSettings,
            onRefresh = component::refresh,
            onSearchChange = component::setSearch,
            onListDisplayOptionChange = component::setListVDisplayOption,
            modifier = modifier
        )

        HomeComponent.Model.Loading -> LoadingView(modifier.padding(contentPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    model: HomeComponent.Model.Content,
    contentPadding: PaddingValues,
    navigateToDetailScreen: (Int) -> Unit,
    navigateToFilterScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    onRefresh: () -> Unit,
    onSearchChange: (String) -> Unit,
    onListDisplayOptionChange: (ListDisplayOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagingItems = model.games.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
    val currentListDisplayOption = model.listDisplayOption
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    BoxWithConstraints {
        val itemWidth = when (currentListDisplayOption) {
            ListDisplayOption.Grid -> 160.dp
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
                if (pagingItems.loadState.refresh !is LoadState.Error || model.search.isNotEmpty()) {
                    HomeAppBar(
                        searchText = model.search,
                        onSearchChange = onSearchChange,
                        isFilterEdited = model.isFilterEdited,
                        currentListDisplayOption = currentListDisplayOption,
                        onListDisplayOptionChange = onListDisplayOptionChange,
                        scrollBehavior = scrollBehavior,
                        navigateToFilterScreen = navigateToFilterScreen,
                        navigateToSettingsScreen = navigateToSettingsScreen,
                    )
                }
            }
        ) { paddingValues ->
            GamesView(
                pagingItems = pagingItems,
                listDisplayMode = currentListDisplayOption,
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