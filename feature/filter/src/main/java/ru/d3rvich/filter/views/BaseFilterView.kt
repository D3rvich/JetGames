package ru.d3rvich.filter.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.filter.R

/**
 * Created by Ilya Deryabin at 03.05.2024
 */
@Composable
internal fun BaseFilterView(
    label: String,
    trailingIcon: @Composable (isInnerContainerVisible: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    selectedItemView: (@Composable () -> Unit)? = null,
    innerContent: @Composable () -> Unit,
) {
    var isInnerContentVisible by rememberSaveable {
        mutableStateOf(false)
    }
    BaseFilterView(
        modifier = modifier,
        label = label,
        isInnerContainerVisible = isInnerContentVisible,
        onInnerContainerVisibilityChange = { isInnerContentVisible = it },
        selectedItem = selectedItemView,
        trailingIcon = trailingIcon,
        innerContent = innerContent
    )
}

@Composable
internal fun BaseFilterView(
    label: String,
    isInnerContainerVisible: Boolean,
    onInnerContainerVisibilityChange: (isInnerContainerVisible: Boolean) -> Unit,
    trailingIcon: @Composable (isInnerContainerVisible: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    selectedItem: (@Composable () -> Unit)? = null,
    innerContent: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onInnerContainerVisibilityChange(!isInnerContainerVisible)
                }
                .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            selectedItem?.invoke()
            trailingIcon(isInnerContainerVisible)
        }
        ChangeVisibilityContainer(visible = isInnerContainerVisible) {
            innerContent()
        }
    }
}

@Composable
internal fun <T> BaseListSelectFilterView(
    label: String,
    selectedItems: List<T>,
    itemName: (item: T) -> String,
    onRemoveSelectedItem: (item: T) -> Unit,
    onClearSelectedItems: () -> Unit,
    onRequestSelectDialog: () -> Unit,
    modifier: Modifier = Modifier,
    itemKey: ((item: T) -> Any)? = null,
) {
    val listHolder by remember(selectedItems) {
        derivedStateOf { selectedItems.isNotEmpty() }
    }
    var showInnerContent by rememberSaveable(listHolder) {
        mutableStateOf(false)
    }
    BaseFilterView(
        modifier = modifier,
        label = label,
        isInnerContainerVisible = showInnerContent,
        onInnerContainerVisibilityChange = {
            if (selectedItems.isEmpty()) {
                onRequestSelectDialog()
            } else {
                showInnerContent = it
            }
        },
        trailingIcon = {
            ChangeVisibilityContainerDefaults.MultiStateIcon(
                iconDirection = when {
                    selectedItems.isEmpty() -> IconDirection.Right
                    showInnerContent -> IconDirection.Down
                    else -> IconDirection.Up
                }
            )
        },
        innerContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                InnerContentListHeader(
                    onEditButtonClicked = onRequestSelectDialog,
                    onClearAllButtonClicked = onClearSelectedItems
                )
                InnerContentSelectedItems(
                    items = selectedItems,
                    itemName = itemName,
                    itemKey = itemKey,
                    onRemoveItem = { onRemoveSelectedItem(it) })
            }
        })
}

@Composable
internal fun InnerContentListHeader(
    onEditButtonClicked: () -> Unit,
    onClearAllButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = onEditButtonClicked) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                    Text(text = stringResource(id = R.string.edit))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = onClearAllButtonClicked) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                    Text(text = stringResource(id = R.string.clear_all))
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun <T> InnerContentSelectedItems(
    items: List<T>,
    itemName: (item: T) -> String,
    onRemoveItem: (item: T) -> Unit,
    modifier: Modifier = Modifier,
    itemKey: ((item: T) -> Any)? = null,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = items, key = itemKey) { item ->
            Row(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = itemName(item))
                IconButton(onClick = { onRemoveItem(item) }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseSectionClosedPreview() {
    JetGamesTheme {
        BaseFilterView(
            label = "Preview label",
            isInnerContainerVisible = false,
            onInnerContainerVisibilityChange = {},
            trailingIcon = {},
            innerContent = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseSectionOpenPreview() {
    JetGamesTheme {
        BaseFilterView(
            label = "Preview label",
            isInnerContainerVisible = true,
            onInnerContainerVisibilityChange = {},
            trailingIcon = {},
            innerContent = {
                Text(
                    text = "Inner content",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    textAlign = TextAlign.Center
                )
            })
    }
}