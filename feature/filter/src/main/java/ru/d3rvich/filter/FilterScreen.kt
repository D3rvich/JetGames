package ru.d3rvich.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.RangeSlider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.preferences.isDefault
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.filter.model.FilterUiAction
import ru.d3rvich.filter.model.FilterUiEvent
import ru.d3rvich.filter.model.ListAction
import ru.d3rvich.filter.views.BaseSection
import ru.d3rvich.filter.views.BaseSelectedListSection
import ru.d3rvich.filter.views.ChangeVisibilityContainerDefaults
import ru.d3rvich.filter.views.FilterAppBar
import ru.d3rvich.filter.views.SortingView
import kotlin.math.roundToInt

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController) {
    val viewModel: FilterViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    var specToShow: FilterSpecToShow? by rememberSaveable {
        mutableStateOf(null)
    }
    val showResetButton: Boolean by remember(state.filterPreferencesBody) {
        derivedStateOf { !state.filterPreferencesBody.isDefault() }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
        topBar = {
            FilterAppBar(
                isResetButtonVisible = showResetButton,
                onBackClicked = { navController.popBackStack() },
                onResetClicked = { viewModel.obtainEvent(FilterUiEvent.OnResetClicked) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.obtainEvent(FilterUiEvent.OnApplyClicked) }) {
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
            GenresSection(
                selectedGenres = state.filterPreferencesBody.selectedGenres,
                onRemoveGenre = {
                    viewModel.obtainEvent(
                        FilterUiEvent.OnSelectedGenresChange(
                            ListAction.RemoveItem(it)
                        )
                    )
                },
                onClearRequest = {
                    viewModel.obtainEvent(
                        FilterUiEvent.OnSelectedGenresChange(
                            ListAction.Clear()
                        )
                    )
                },
                requestGenresDialog = { specToShow = FilterSpecToShow.Genres })
            PlatformsSection(
                selectedPlatforms = state.filterPreferencesBody.selectedPlatforms,
                onRemovePlatform = {
                    viewModel.obtainEvent(
                        FilterUiEvent.OnSelectedPlatformsChange(
                            ListAction.RemoveItem(it)
                        )
                    )
                },
                onClearRequest = {
                    viewModel.obtainEvent(
                        FilterUiEvent.OnSelectedPlatformsChange(
                            ListAction.Clear()
                        )
                    )
                },
                requestPlatformsDialog = { specToShow = FilterSpecToShow.Platform })
            SortingSection(
                sortingList = state.sortingList,
                selectedSorting = state.filterPreferencesBody.sortBy,
                isSortReversed = state.filterPreferencesBody.isReversed,
                onSortingSelected = { sorting ->
                    viewModel.obtainEvent(FilterUiEvent.OnSortChange(sorting))
                },
                onReversedChange = { reversed ->
                    viewModel.obtainEvent(FilterUiEvent.OnReversedChange(reversed))
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
            MetacriticSection(
                range = metacriticRange,
                onRangeChange = { floatRange ->
                    val range = MetacriticRange(floatRange = floatRange)
                    viewModel.obtainEvent(FilterUiEvent.OnMetacriticRangeChange(range))
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
                                viewModel.obtainEvent(
                                    FilterUiEvent.OnSelectedGenresChange(
                                        ListAction.AddItem(it)
                                    )
                                )
                            },
                            onItemRemoved = {
                                viewModel.obtainEvent(
                                    FilterUiEvent.OnSelectedGenresChange(
                                        ListAction.RemoveItem(it)
                                    )
                                )
                            },
                            getItemName = GenreFullEntity::name
                        )
                    }

                    FilterSpecToShow.Platform -> {
                        BottomSheetContent(
                            items = state.platforms.sortedBy { it.name },
                            selectedItems = state.filterPreferencesBody.selectedPlatforms,
                            onItemSelected = {
                                viewModel.obtainEvent(
                                    FilterUiEvent.OnSelectedPlatformsChange(
                                        ListAction.AddItem(it)
                                    )
                                )
                            },
                            onItemRemoved = {
                                viewModel.obtainEvent(
                                    FilterUiEvent.OnSelectedPlatformsChange(
                                        ListAction.RemoveItem(it)
                                    )
                                )
                            },
                            getItemName = PlatformEntity::name
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiAction.collect { action ->
            when (action) {
                FilterUiAction.NavigateBack -> navController.popBackStack()
            }
        }
    }
}

@Composable
internal fun MetacriticSection(
    modifier: Modifier = Modifier,
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    BaseSection(
        modifier = modifier,
        label = stringResource(id = R.string.metacritic_label),
        icon = { isOpen ->
            ChangeVisibilityContainerDefaults.DefaultIcon(isOpen = isOpen)
        }) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RangeSlider(
                valueRange = 0f..100f,
                value = range,
                onValueChange = onRangeChange
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = range.start.roundToInt().toString())
                Text(text = range.endInclusive.roundToInt().toString())
            }
        }
    }
}

@Composable
private fun <T> BottomSheetContent(
    items: List<T>,
    selectedItems: List<T>,
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

@Composable
internal fun PlatformsSection(
    modifier: Modifier = Modifier,
    selectedPlatforms: List<PlatformEntity>,
    onRemovePlatform: (PlatformEntity) -> Unit,
    onClearRequest: () -> Unit,
    requestPlatformsDialog: () -> Unit,
) {
    BaseSelectedListSection(
        modifier = modifier,
        label = stringResource(id = R.string.platforms_label, selectedPlatforms.size),
        selectedItems = selectedPlatforms,
        itemName = PlatformEntity::name,
        itemKey = PlatformEntity::id,
        onRemoveItem = onRemovePlatform,
        onClear = onClearRequest,
        requestSelectDialog = requestPlatformsDialog
    )
}

@Composable
internal fun GenresSection(
    modifier: Modifier = Modifier,
    selectedGenres: List<GenreFullEntity>,
    onRemoveGenre: (GenreFullEntity) -> Unit,
    onClearRequest: () -> Unit,
    requestGenresDialog: () -> Unit,
) {
    BaseSelectedListSection(
        modifier = modifier,
        label = stringResource(id = R.string.genres_label, selectedGenres.size),
        selectedItems = selectedGenres,
        itemName = GenreFullEntity::name,
        itemKey = GenreFullEntity::id,
        onRemoveItem = onRemoveGenre,
        onClear = onClearRequest,
        requestSelectDialog = requestGenresDialog
    )
}

@Composable
private fun SortingSection(
    modifier: Modifier = Modifier,
    sortingList: List<SortingEntity>,
    selectedSorting: SortingEntity,
    isSortReversed: Boolean,
    onSortingSelected: (SortingEntity) -> Unit,
    onReversedChange: (isReversed: Boolean) -> Unit,
) {
    BaseSection(
        modifier = modifier,
        label = stringResource(id = R.string.sort_by_label),
        icon = { isOpen -> ChangeVisibilityContainerDefaults.DefaultIcon(isOpen = isOpen) },
        selectedItem = {
            AnimatedContent(
                targetState = selectedSorting,
                transitionSpec = {
                    when {
                        initialState == SortingEntity.NoSorting ||
                                targetState == SortingEntity.NoSorting -> {
                            fadeIn() togetherWith fadeOut()
                        }

                        else -> slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                },
                label = "textAnimation"
            ) { entity ->
                Text(text = if (entity != SortingEntity.NoSorting) entity.name else "")
            }
        }) {
        SortingView(
            sortingList = sortingList,
            selectedSorting = selectedSorting,
            isReversed = isSortReversed,
            onSortingSelected = onSortingSelected,
            onReversedChange = onReversedChange
        )
    }
}

private enum class FilterSpecToShow {
    Platform,
    Genres
}