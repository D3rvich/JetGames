package ru.d3rvich.filter.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.filter.R

@Composable
internal fun PlatformsView(
    selectedPlatforms: List<PlatformEntity>,
    modifier: Modifier = Modifier,
    onRemovePlatform: (PlatformEntity) -> Unit,
    onClearRequest: () -> Unit,
    requestPlatformsDialog: () -> Unit,
) {
    BaseListSelectFilterView(
        modifier = modifier,
        label = stringResource(id = R.string.platforms_label, selectedPlatforms.size),
        selectedItems = selectedPlatforms,
        itemName = PlatformEntity::name,
        itemKey = PlatformEntity::id,
        onRemoveSelectedItem = onRemovePlatform,
        onClearSelectedItems = onClearRequest,
        onRequestSelectDialog = requestPlatformsDialog
    )
}