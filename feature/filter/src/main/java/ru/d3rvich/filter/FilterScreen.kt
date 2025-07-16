package ru.d3rvich.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.preferences.isDefault
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.filter.model.FilterUiAction
import ru.d3rvich.filter.model.FilterUiEvent
import ru.d3rvich.filter.model.FilterUiState
import ru.d3rvich.filter.model.ListAction
import ru.d3rvich.filter.views.FilterAppBar
import ru.d3rvich.filter.views.GenresView
import ru.d3rvich.filter.views.MetacriticView
import ru.d3rvich.filter.views.PlatformsView
import ru.d3rvich.filter.views.SortingView

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
@Composable
fun FilterScreen(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: FilterViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    FilterScreen(
        modifier = modifier,
        state = state,
        onApply = { viewModel.obtainEvent(FilterUiEvent.OnApplyClicked) },
        onReset = { viewModel.obtainEvent(FilterUiEvent.OnResetClicked) },
        onNavigateBack = { navController.popBackStack() },
        onSelectedGenresChange = { listAction ->
            viewModel.obtainEvent(FilterUiEvent.OnSelectedGenresChange(listAction))
        },
        onSelectedPlatformsChange = { listAction ->
            viewModel.obtainEvent(FilterUiEvent.OnSelectedPlatformsChange(listAction))
        },
        onSortChange = { sortingEntity ->
            viewModel.obtainEvent(FilterUiEvent.OnSortChange(sortingEntity))
        },
        onSortReversedChange = { isReversed ->
            viewModel.obtainEvent(FilterUiEvent.OnReversedChange(isReversed))
        },
        onMetacriticRangeChange = { metacriticRange ->
            viewModel.obtainEvent(FilterUiEvent.OnMetacriticRangeChange(metacriticRange))
        },
    )
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { action ->
            when (action) {
                FilterUiAction.NavigateBack -> navController.popBackStack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterScreen(
    state: FilterUiState,
    modifier: Modifier = Modifier,
    onApply: () -> Unit,
    onReset: () -> Unit,
    onNavigateBack: () -> Unit,
    onSelectedGenresChange: (ListAction<GenreFullEntity>) -> Unit,
    onSelectedPlatformsChange: (ListAction<PlatformEntity>) -> Unit,
    onSortChange: (SortingEntity) -> Unit,
    onSortReversedChange: (Boolean) -> Unit,
    onMetacriticRangeChange: (MetacriticRange) -> Unit,
) {
    var specToShow: FilterSpecToShow? by rememberSaveable {
        mutableStateOf(null)
    }
    val showResetButton: Boolean by remember(state.filterPreferencesBody) {
        derivedStateOf { !state.filterPreferencesBody.isDefault() }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
        topBar = {
            FilterAppBar(
                isResetButtonVisible = showResetButton,
                onBackClicked = onNavigateBack,
                onResetClicked = onReset,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onApply) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.apply_filter)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GenresView(
                selectedGenres = state.filterPreferencesBody.selectedGenres,
                onRemoveGenre = {
                    onSelectedGenresChange(ListAction.RemoveItem(it))
                },
                onClearRequest = {
                    onSelectedGenresChange(ListAction.Clear())
                },
                requestGenresDialog = { specToShow = FilterSpecToShow.Genres })
            PlatformsView(
                selectedPlatforms = state.filterPreferencesBody.selectedPlatforms,
                onRemovePlatform = { item ->
                    onSelectedPlatformsChange(ListAction.RemoveItem(item))
                },
                onClearRequest = {
                    onSelectedPlatformsChange(ListAction.Clear())
                },
                requestPlatformsDialog = { specToShow = FilterSpecToShow.Platform })
            SortingView(
                sortingList = state.sortingList,
                selectedSorting = state.filterPreferencesBody.sortBy,
                isSortReversed = state.filterPreferencesBody.isReversed,
                onSortingSelected = { sorting ->
                    onSortChange(sorting)
                },
                onReversedChange = { isReversed ->
                    onSortReversedChange(isReversed)
                }
            )
            val metacriticRange =
                if (state.filterPreferencesBody.metacriticRange != MetacriticRange.None) {
                    with(state.filterPreferencesBody.metacriticRange) {
                        min..max
                    }
                } else {
                    0f..100f
                }
            MetacriticView(
                range = metacriticRange,
                onRangeChange = { floatRange ->
                    val range = MetacriticRange(floatRange = floatRange)
                    onMetacriticRangeChange(range)
                },
            )
        }
        specToShow?.let { specToShowNotNull ->
            ModalBottomSheet(
                onDismissRequest = { specToShow = null },
            ) {
                when (specToShowNotNull) {
                    FilterSpecToShow.Genres -> {
                        BottomSheetContent(
                            items = state.genres.sortedBy { it.name },
                            selectedItems = state.filterPreferencesBody.selectedGenres,
                            onItemSelected = {
                                onSelectedGenresChange(ListAction.AddItem(it))
                            },
                            onItemRemoved = {
                                FilterUiEvent.OnSelectedGenresChange(ListAction.RemoveItem(it))
                            },
                            getItemName = GenreFullEntity::name
                        )
                    }

                    FilterSpecToShow.Platform -> {
                        BottomSheetContent(
                            items = state.platforms.sortedBy { it.name },
                            selectedItems = state.filterPreferencesBody.selectedPlatforms,
                            onItemSelected = {
                                onSelectedPlatformsChange(ListAction.AddItem(it))
                            },
                            onItemRemoved = {
                                onSelectedPlatformsChange(ListAction.RemoveItem(it))
                            },
                            getItemName = PlatformEntity::name
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> BottomSheetContent(
    items: List<T>,
    selectedItems: List<T>,
    modifier: Modifier = Modifier,
    onItemSelected: (T) -> Unit,
    onItemRemoved: (T) -> Unit,
    getItemName: (T) -> String,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        items(items) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .toggleable(
                        value = item in selectedItems,
                        onValueChange = { selected ->
                            if (selected) {
                                onItemSelected(item)
                            } else {
                                onItemRemoved(item)
                            }
                        },
                        role = Role.Checkbox
                    )
                    .height(56.dp)
                    .padding(16.dp)
            ) {
                Checkbox(
                    checked = item in selectedItems,
                    onCheckedChange = null
                )
                Text(
                    text = getItemName(item),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

private enum class FilterSpecToShow {
    Platform,
    Genres
}

@Preview(showBackground = true)
@Composable
private fun FilterScreenPreview() {
    JetGamesTheme {
        val state = FilterUiState(
            sortingList = SortingEntity.entries,
            platforms = emptyList(),
            genres = emptyList(),
            filterPreferencesBody = FilterPreferencesBody.default()
        )
        FilterScreen(
            state = state,
            onApply = { },
            onReset = { },
            onNavigateBack = { },
            onSelectedGenresChange = { },
            onSelectedPlatformsChange = { },
            onSortChange = { },
            onSortReversedChange = { },
            onMetacriticRangeChange = { },
        )
    }
}