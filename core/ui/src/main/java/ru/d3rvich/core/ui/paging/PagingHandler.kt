package ru.d3rvich.core.ui.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

@DslMarker
annotation class PagingDSL

@PagingDSL
class PagingHandlerScope<T : Any> internal constructor(private val items: LazyPagingItems<T>) {
    val loadState get() = items.loadState
    val isEmpty get() = loadState.refresh !is LoadState.Error && items.itemCount == 0
    val isLoading get() = loadState.refresh == LoadState.Loading
    val isError get() = loadState.refresh is LoadState.Error

    fun LazyListScope.pagingItems(
        key: ((T) -> Any)? = null,
        contentType: ((T) -> Any)? = null,
        itemContent: @Composable LazyItemScope.(item: T) -> Unit
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(key),
            contentType = items.itemContentType(contentType)
        ) { index ->
            items[index]?.let { item ->
                itemContent(item)
            }
        }
    }

    fun LazyListScope.appendLoadingItem(content: @Composable LazyItemScope.() -> Unit) {
        if (loadState.append == LoadState.Loading) {
            item(contentType = "PagingAppendLoading") { content() }
        }
    }

    fun LazyListScope.endOfPaginationItem(content: @Composable LazyItemScope.() -> Unit) {
        if (loadState.append.endOfPaginationReached) {
            item(contentType = "PagingAppendEnd") { content() }
        }
    }
}

@Composable
fun <T : Any> HandlePagingItems(
    items: LazyPagingItems<T>,
    onLoading: @Composable () -> Unit = {},
    onEmpty: @Composable () -> Unit = {},
    onError: @Composable (error: Throwable) -> Unit = {},
    onSuccess: @Composable PagingHandlerScope<T>.() -> Unit,
) {
    val scope = remember { PagingHandlerScope(items) }

    when {
        scope.isLoading -> onLoading()
        scope.isEmpty -> onEmpty()
        scope.isError -> {
            val errorState = scope.loadState.refresh as LoadState.Error
            onError(errorState.error)
        }

        else -> scope.onSuccess()
    }
}