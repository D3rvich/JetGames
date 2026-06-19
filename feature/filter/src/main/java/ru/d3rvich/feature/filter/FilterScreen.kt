package ru.d3rvich.feature.filter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.filter.model.ListAction
import ru.d3rvich.feature.filter.model.isDefault
import ru.d3rvich.feature.filter.model.toFilterPreferencesBodyUiModel
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
        onIntent = viewModel::obtainIntent,
        onNavigateBack = onNavigateBack,
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
    onIntent: (FilterStore.Intent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var specToShow: FilterSpecToShow? by rememberSaveable {
        mutableStateOf(null)
    }
    val showResetButton: Boolean by remember(state) {
        derivedStateOf { !state.filterPreferencesBody.isDefault() }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        topBar = {
            FilterAppBar(
                isResetButtonVisible = showResetButton,
                onBackClicked = onNavigateBack,
                onResetClicked = { onIntent(FilterStore.Intent.OnResetClicked) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onIntent(FilterStore.Intent.OnApplyClicked) }) {
                Icon(
                    painter = painterResource(uiR.drawable.check_24px),
                    contentDescription = stringResource(R.string.apply_filter)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                GenresView(
                    selectedGenres = state.filterPreferencesBody.selectedGenres,
                    onRemoveGenre = {
                        onIntent(FilterStore.Intent.OnSelectedGenresChange(ListAction.RemoveItem(it)))
                    },
                    onClearRequest = {
                        onIntent(FilterStore.Intent.OnSelectedGenresChange(ListAction.Clear()))
                    },
                    requestGenresDialog = { specToShow = FilterSpecToShow.Genres })
            }
            item {
                PlatformsView(
                    selectedPlatforms = state.filterPreferencesBody.selectedPlatforms,
                    onRemovePlatform = { item ->
                        onIntent(
                            FilterStore.Intent.OnSelectedPlatformsChange(
                                ListAction.RemoveItem(item)
                            )
                        )
                    },
                    onClearRequest = {
                        onIntent(FilterStore.Intent.OnSelectedPlatformsChange(ListAction.Clear()))
                    },
                    requestPlatformsDialog = { specToShow = FilterSpecToShow.Platform })
            }
            item {
                SortingView(
                    sortingList = state.sortingList,
                    selectedSorting = state.filterPreferencesBody.sortBy,
                    isSortReversed = state.filterPreferencesBody.isReversed,
                    onSortingSelected = { sorting ->
                        onIntent(FilterStore.Intent.OnSortChange(sorting))
                    },
                    onReversedChange = { isReversed ->
                        onIntent(FilterStore.Intent.OnReversedChange(isReversed))
                    }
                )
            }
            item {
                val metacriticRange =
                    if (state.filterPreferencesBody.metacriticRange != MetacriticRange.Unspecific) {
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
                        onIntent(FilterStore.Intent.OnMetacriticRangeChange(range))
                    },
                )
            }
        }
        specToShow?.let { specToShowNotNull ->
            FilterBottomSheet(
                spec = specToShowNotNull,
                state = state,
                onIntent = onIntent,
                onDismiss = { specToShow = null })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    spec: FilterSpecToShow,
    state: FilterStore.State,
    onIntent: (FilterStore.Intent) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        when (spec) {
            FilterSpecToShow.Genres -> BottomSheetContent(
                items = state.genres,
                selectedItems = state.filterPreferencesBody.selectedGenres,
                onItemSelected = {
                    onIntent(
                        FilterStore.Intent.OnSelectedGenresChange(
                            ListAction.AddItem(it)
                        )
                    )
                },
                onItemRemoved = {
                    onIntent(
                        FilterStore.Intent.OnSelectedGenresChange(
                            ListAction.RemoveItem(it)
                        )
                    )
                },
                itemName = GenreFullEntity::name,
                itemKey = GenreFullEntity::id
            )

            FilterSpecToShow.Platform -> BottomSheetContent(
                items = state.platforms,
                selectedItems = state.filterPreferencesBody.selectedPlatforms,
                onItemSelected = {
                    onIntent(
                        FilterStore.Intent.OnSelectedPlatformsChange(
                            ListAction.AddItem(it)
                        )
                    )
                },
                onItemRemoved = {
                    onIntent(
                        FilterStore.Intent.OnSelectedPlatformsChange(
                            ListAction.RemoveItem(it)
                        )
                    )
                },
                itemName = PlatformEntity::name,
                itemKey = PlatformEntity::id
            )
        }
    }
}

@Composable
private fun <T> BottomSheetContent(
    items: ImmutableList<T>,
    selectedItems: ImmutableSet<T>,
    onItemSelected: (T) -> Unit,
    onItemRemoved: (T) -> Unit,
    itemKey: (T) -> Any,
    itemName: (T) -> String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        items(items, key = itemKey) { item ->
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
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(16.dp)
            ) {
                Checkbox(
                    checked = item in selectedItems,
                    onCheckedChange = null
                )
                Text(
                    text = itemName(item),
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
            platforms = persistentListOf(),
            genres = persistentListOf(),
            filterPreferencesBody = FilterPreferencesBody.default().toFilterPreferencesBodyUiModel()
        )
        FilterScreen(
            state = state,
            onIntent = {},
            onNavigateBack = { },
        )
    }
}