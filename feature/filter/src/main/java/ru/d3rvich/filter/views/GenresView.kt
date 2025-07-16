package ru.d3rvich.filter.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.filter.R

@Composable
internal fun GenresView(
    selectedGenres: List<GenreFullEntity>,
    modifier: Modifier = Modifier,
    onRemoveGenre: (GenreFullEntity) -> Unit,
    onClearRequest: () -> Unit,
    requestGenresDialog: () -> Unit,
) {
    BaseListSelectFilterView(
        modifier = modifier,
        label = stringResource(id = R.string.genres_label, selectedGenres.size),
        selectedItems = selectedGenres,
        itemName = GenreFullEntity::name,
        itemKey = GenreFullEntity::id,
        onRemoveSelectedItem = onRemoveGenre,
        onClearSelectedItems = onClearRequest,
        onRequestSelectDialog = requestGenresDialog
    )
}