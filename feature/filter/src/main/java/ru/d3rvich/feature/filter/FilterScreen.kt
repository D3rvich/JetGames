package ru.d3rvich.feature.filter

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import org.koin.compose.viewmodel.koinViewModel
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.preferences.isDefault
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.filter.model.ListAction
import ru.d3rvich.feature.filter.store.FilterStore
import ru.d3rvich.feature.filter.views.FilterAppBar
import ru.d3rvich.feature.filter.views.GenresView
import ru.d3rvich.feature.filter.views.MetacriticView
import ru.d3rvich.feature.filter.views.PlatformsView
import ru.d3rvich.feature.filter.views.SortingView
import ru.d3rvich.common.R as uiR

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
@Composable
fun FilterScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilterViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FilterScreen(
        modifier = modifier,
        state = state,
        onApply = { viewModel.obtainIntent(FilterStore.Intent.OnApplyClicked) },
        onReset = { viewModel.obtainIntent(FilterStore.Intent.OnResetClicked) },
        onNavigateBack = onNavigateBack,
        onSelectedGenresChange = { listAction ->
            viewModel.obtainIntent(FilterStore.Intent.OnSelectedGenresChange(listAction))
        },
        onSelectedPlatformsChange = { listAction ->
            viewModel.obtainIntent(FilterStore.Intent.OnSelectedPlatformsChange(listAction))
        },
        onSortChange = { sortingEntity ->
            viewModel.obtainIntent(FilterStore.Intent.OnSortChange(sortingEntity))
        },
        onSortReversedChange = { isReversed ->
            viewModel.obtainIntent(FilterStore.Intent.OnReversedChange(isReversed))
        },
        onMetacriticRangeChange = { metacriticRange ->
            viewModel.obtainIntent(FilterStore.Intent.OnMetacriticRangeChange(metacriticRange))
        },
    )
    LaunchedEffect(viewModel) {
        viewModel.labels.collect { label ->
            when (label) {
                FilterStore.Label.CloseScreen -> onNavigateBack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterScreen(
    state: FilterStore.State,
    onApply: () -> Unit,
    onReset: () -> Unit,
    onNavigateBack: () -> Unit,
    onSelectedGenresChange: (ListAction<GenreFullEntity>) -> Unit,
    onSelectedPlatformsChange: (ListAction<PlatformEntity>) -> Unit,
    onSortChange: (SortingEntity) -> Unit,
    onSortReversedChange: (Boolean) -> Unit,
    onMetacriticRangeChange: (MetacriticRange) -> Unit,
    modifier: Modifier = Modifier,
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
                    painter = painterResource(uiR.drawable.check_24px),
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
                selectedGenres = state.filterPreferencesBody.selectedGenres.toImmutableSet(),
                onRemoveGenre = {
                    onSelectedGenresChange(ListAction.RemoveItem(it))
                },
                onClearRequest = {
                    onSelectedGenresChange(ListAction.Clear())
                },
                requestGenresDialog = { specToShow = FilterSpecToShow.Genres })
            PlatformsView(
                selectedPlatforms = state.filterPreferencesBody.selectedPlatforms.toImmutableSet(),
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
                            items = state.genres,
                            selectedItems = state.filterPreferencesBody.selectedGenres.toImmutableSet(),
                            onItemSelected = {
                                onSelectedGenresChange(ListAction.AddItem(it))
                            },
                            onItemRemoved = {
                                onSelectedGenresChange(ListAction.RemoveItem(it))
                            },
                            getItemName = GenreFullEntity::name
                        )
                    }

                    FilterSpecToShow.Platform -> {
                        BottomSheetContent(
                            items = state.platforms,
                            selectedItems = state.filterPreferencesBody.selectedPlatforms.toImmutableSet(),
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
    items: ImmutableList<T>,
    selectedItems: ImmutableSet<T>,
    onItemSelected: (T) -> Unit,
    onItemRemoved: (T) -> Unit,
    getItemName: (T) -> String,
    modifier: Modifier = Modifier,
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
        val state = FilterStore.State(
            sortingList = SortingEntity.entries.toImmutableList(),
            platforms = emptyList<PlatformEntity>().toImmutableList(),
            genres = emptyList<GenreFullEntity>().toImmutableList(),
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