package ru.d3rvich.feature.filter.ui

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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableSet
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.entity.SortingEntity
import ru.d3rvich.core.model.FilterPreferencesBody
import ru.d3rvich.core.model.MetacriticRange
import ru.d3rvich.core.model.isDefault
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.filter.api.FilterComponent
import ru.d3rvich.feature.filter.api.ListAction
import ru.d3rvich.feature.filter.ui.views.FilterAppBar
import ru.d3rvich.feature.filter.ui.views.GenresView
import ru.d3rvich.feature.filter.ui.views.MetacriticView
import ru.d3rvich.feature.filter.ui.views.PlatformsView
import ru.d3rvich.feature.filter.ui.views.SortingView
import ru.d3rvich.common.R as commonR

@Composable
fun FilterContent(component: FilterComponent, modifier: Modifier = Modifier) {
    val model by component.models.subscribeAsState()
    FilterContent(
        model = model,
        onBackClicked = component::close,
        onReset = component::reset,
        onApply = component::apply,
        onSortingChange = component::setSorting,
        onReversedChange = component::setIsReversed,
        onSelectedPlatformsChange = component::updateSelectedPlatforms,
        onSelectedGenresChange = component::updateSelectedGenres,
        onMetacriticRangeChange = component::setMetacriticRange,
        modifier = modifier
    )
}

@Composable
private fun FilterContent(
    model: FilterComponent.Model,
    onBackClicked: () -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onSortingChange: (SortingEntity) -> Unit,
    onReversedChange: (Boolean) -> Unit,
    onSelectedPlatformsChange: (ListAction<PlatformEntity>) -> Unit,
    onSelectedGenresChange: (ListAction<GenreFullEntity>) -> Unit,
    onMetacriticRangeChange: (MetacriticRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    var specToShow: FilterSpecToShow? by rememberSaveable {
        mutableStateOf(null)
    }
    val showResetButton: Boolean by remember(model) {
        derivedStateOf { !model.filterPreferencesBody.isDefault() }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        topBar = {
            FilterAppBar(
                isResetButtonVisible = showResetButton,
                onBackClicked = onBackClicked,
                onResetClicked = onReset,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onApply) {
                Icon(
                    painter = painterResource(commonR.drawable.check_24px),
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
                    selectedGenres = model.filterPreferencesBody.selectedGenres.toImmutableSet(),
                    onRemoveGenre = {
                        onSelectedGenresChange(ListAction.RemoveItem(it))
                    },
                    onClearRequest = {
                        onSelectedGenresChange(ListAction.Clear())
                    },
                    requestGenresDialog = {
                        specToShow = FilterSpecToShow.Genres
                    })
            }
            item {
                PlatformsView(
                    selectedPlatforms = model.filterPreferencesBody.selectedPlatforms.toImmutableSet(),
                    onRemovePlatform = { item ->
                        onSelectedPlatformsChange(ListAction.RemoveItem(item))
                    },
                    onClearRequest = {
                        onSelectedPlatformsChange(ListAction.Clear())
                    },
                    requestPlatformsDialog = {
                        specToShow = FilterSpecToShow.Platform
                    })
            }
            item {
                SortingView(
                    sortingList = model.sortingList,
                    selectedSorting = model.filterPreferencesBody.sortBy,
                    isSortReversed = model.filterPreferencesBody.isReversed,
                    onSortingSelected = onSortingChange,
                    onReversedChange = onReversedChange
                )
            }
            item {
                val metacriticRange =
                    if (model.filterPreferencesBody.metacriticRange != MetacriticRange.Unspecific) {
                        with(model.filterPreferencesBody.metacriticRange) {
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
        }
        specToShow?.let { specToShowNotNull ->
            FilterBottomSheet(
                spec = specToShowNotNull,
                model = model,
                onSelectedPlatformsChange = onSelectedPlatformsChange,
                onSelectedGenresChange = onSelectedGenresChange,
                onDismiss = { specToShow = null })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    spec: FilterSpecToShow,
    model: FilterComponent.Model,
    onSelectedPlatformsChange: (ListAction<PlatformEntity>) -> Unit,
    onSelectedGenresChange: (ListAction<GenreFullEntity>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        when (spec) {
            FilterSpecToShow.Genres -> BottomSheetContent(
                items = model.genres,
                selectedItems = model.filterPreferencesBody.selectedGenres.toImmutableSet(),
                onItemSelected = {
                    onSelectedGenresChange(ListAction.AddItem(it))
                },
                onItemRemoved = {
                    onSelectedGenresChange(ListAction.RemoveItem(it))
                },
                itemName = GenreFullEntity::name,
                itemKey = GenreFullEntity::id
            )

            FilterSpecToShow.Platform -> BottomSheetContent(
                items = model.platforms,
                selectedItems = model.filterPreferencesBody.selectedPlatforms.toImmutableSet(),
                onItemSelected = {
                    onSelectedPlatformsChange(ListAction.AddItem(it))
                },
                onItemRemoved = {
                    onSelectedPlatformsChange(ListAction.RemoveItem(it))
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
private fun FilterContentPreview() {
    JetGamesTheme {
        val model = FilterComponent.Model(
            sortingList = persistentListOf(),
            platforms = persistentListOf(),
            genres = persistentListOf(),
            filterPreferencesBody = FilterPreferencesBody.default()
        )
        FilterContent(model, {}, {}, {}, {}, {}, {}, {}, {})
    }
}