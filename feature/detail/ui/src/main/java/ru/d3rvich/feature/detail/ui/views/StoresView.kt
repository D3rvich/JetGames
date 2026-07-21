package ru.d3rvich.feature.detail.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.d3rvich.core.entity.StoreEntity
import ru.d3rvich.core.ui.icon.tryFindIcon
import ru.d3rvich.feature.detail.ui.R
import ru.d3rvich.feature.detail.api.LoadingModel

@Composable
internal fun StoresView(
    stores: LoadingModel<ImmutableList<StoreEntity>>,
    onSelected: (storeUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    GameDetailItem(stringResource(R.string.view_in_stores), modifier) {
        when (stores) {
            is LoadingModel.Error -> DefaultBoxWrapper {
                Text(stores.throwable.localizedMessage ?: "Error")
            }

            LoadingModel.Loading -> DefaultBoxWrapper {
                CircularProgressIndicator()
            }

            is LoadingModel.Success -> if (stores.value.isEmpty()) {
                DefaultBoxWrapper {
                    Text(stringResource(R.string.no_stores_found))
                }
            } else {
                Stores(list = stores.value.toImmutableList(), onSelected = onSelected)
            }
        }
    }
}

@Composable
private fun DefaultBoxWrapper(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .heightIn(min = 120.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun Stores(
    list: ImmutableList<StoreEntity>,
    onSelected: (storeUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        list.forEach { store ->
            Card(
                modifier = modifier,
                onClick = {
                    store.url?.let { storeUrl ->
                        onSelected(storeUrl)
                    }
                }) {
                Row(
                    modifier = modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = store.name)
                    store.tryFindIcon()?.let { icon ->
                        Icon(
                            painter = icon,
                            contentDescription = stringResource(
                                R.string.store_icon,
                                store.name
                            ),
                            modifier = modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoresViewPreview_Loading() {
    StoresView(stores = LoadingModel.Loading, {})
}

@Preview(showBackground = true)
@Composable
private fun StoresViewPreview_Empty() {
    StoresView(stores = LoadingModel.Success(persistentListOf()), {})
}

@Preview(showBackground = true)
@Composable
private fun StoresViewPreview_Success() {
    val stores = persistentListOf(StoreEntity(0, "Steam"), StoreEntity(1, "GOG"))
    StoresView(stores = LoadingModel.Success(stores), {})
}

@Preview(showBackground = true)
@Composable
private fun StoresViewPreview_Error() {
    StoresView(stores = LoadingModel.Error(RuntimeException("Preview error")), {})
}