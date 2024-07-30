package ru.d3rvich.filter.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import ru.d3rvich.filter.R

/**
 * Created by Ilya Deryabin at 03.05.2024
 */
@Composable
internal fun BaseSection(
    modifier: Modifier = Modifier,
    label: String,
    selectedItem: (@Composable () -> Unit)? = null,
    icon: @Composable (Boolean) -> Unit,
    innerContent: @Composable () -> Unit,
) {
    var isInnerContentVisible by rememberSaveable {
        mutableStateOf(false)
    }
    BaseSection(
        modifier = modifier,
        label = label,
        isInnerContainerVisible = isInnerContentVisible,
        onInnerContainerVisibilityChange = { isInnerContentVisible = it },
        selectedItem = selectedItem,
        icon = icon,
        innerContent = innerContent
    )
}

@Composable
internal fun BaseSection(
    modifier: Modifier = Modifier,
    label: String,
    isInnerContainerVisible: Boolean,
    onInnerContainerVisibilityChange: (Boolean) -> Unit,
    selectedItem: (@Composable () -> Unit)? = null,
    icon: @Composable (Boolean) -> Unit,
    innerContent: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier
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
            icon(isInnerContainerVisible)
        }
        ChangeVisibilityContainer(visible = isInnerContainerVisible) {
            innerContent()
        }
    }
}

@Composable
internal fun <T> BaseSelectedListSection(
    modifier: Modifier = Modifier,
    label: String,
    selectedItems: List<T>,
    itemName: (item: T) -> String,
    itemKey: ((item: T) -> Any)? = null,
    onRemoveItem: (item: T) -> Unit,
    onClear: () -> Unit,
    requestSelectDialog: () -> Unit,
) {
    val listHolder by remember(selectedItems) {
        derivedStateOf { selectedItems.isNotEmpty() }
    }
    var showInnerContent by rememberSaveable(listHolder) {
        mutableStateOf(false)
    }
    BaseSection(
        modifier = modifier,
        label = label,
        isInnerContainerVisible = showInnerContent,
        onInnerContainerVisibilityChange = {
            if (selectedItems.isEmpty()) {
                requestSelectDialog()
            }
            else {
                showInnerContent = it
            }
        },
        icon = {
            ChangeVisibilityContainerDefaults.ThreeStateIcon(
                iconDirection = when {
                    selectedItems.isEmpty() -> IconDirection.Right
                    showInnerContent -> IconDirection.Down
                    else -> IconDirection.Up
                }
            )
        }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            InnerContentListHeader(
                onEditButtonClicked = requestSelectDialog,
                onClearAllButtonClicked = onClear
            )
            InnerContentSelectedItems(items = selectedItems,
                itemName = itemName,
                itemKey = itemKey,
                onRemoveItem = { onRemoveItem(it) })
        }
    }
}

@Composable
internal fun InnerContentListHeader(
    modifier: Modifier = Modifier,
    onEditButtonClicked: () -> Unit,
    onClearAllButtonClicked: () -> Unit,
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
    modifier: Modifier = Modifier,
    items: List<T>,
    itemName: (item: T) -> String,
    itemKey: ((item: T) -> Any)? = null,
    onRemoveItem: (item: T) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items, key = itemKey) { item ->
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