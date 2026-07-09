package ru.d3rvich.feature.filter.impl.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableSet
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.feature.filter.impl.R

@Composable
internal fun PlatformsView(
    selectedPlatforms: ImmutableSet<PlatformEntity>,
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