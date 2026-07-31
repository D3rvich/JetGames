package ru.d3rvich.core.ui.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    val isLoading get() = loadState.refresh == LoadState.Loading && items.itemCount == 0
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
            items[index]?.let { item -> itemContent(item) }
        }
    }

    fun LazyGridScope.pagingItems(
        key: ((T) -> Any)? = null,
        span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
        contentType: ((T) -> Any)? = null,
        itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(key),
            span = span?.let { span ->
                { index ->
                    val item = items.peek(index)
                    if (item != null) span(item) else GridItemSpan(1)
                }
            },
            contentType = items.itemContentType(contentType)
        ) { index ->
            items[index]?.let { item -> itemContent(item) }
        }
    }

    fun LazyListScope.appendLoadingItem(content: @Composable LazyItemScope.() -> Unit) {
        if (loadState.append == LoadState.Loading) {
            item(contentType = "PagingAppendLoading") { content() }
        }
    }

    fun LazyGridScope.appendLoadingItem(content: @Composable LazyGridItemScope.() -> Unit) {
        if (loadState.append == LoadState.Loading) {
            item(
                contentType = "PagingAppendLoading",
                span = { GridItemSpan(maxLineSpan) }
            ) { content() }
        }
    }

    fun LazyGridScope.appendLoadingItems(
        itemCount: Int,
        key: ((Int) -> Any)? = { "LoadingItem$it" },
        contentType: ((Int) -> Any)? = { "LoadingItems" },
        span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
        itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit
    ) {
        if (loadState.append == LoadState.Loading) {
            items(
                count = itemCount,
                key = key,
                contentType = { contentType?.invoke(it) },
                span = span?.let { span ->
                    { index ->
                        val item = items.peek(index)
                        if (item != null) span(item) else GridItemSpan(1)
                    }
                }) { index ->
                itemContent(index)
            }
        }
    }
}

@Composable
fun <T : Any> HandlePagingItems(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    onLoading: @Composable () -> Unit = {},
    onEmpty: @Composable () -> Unit = {},
    onError: @Composable (error: Throwable) -> Unit = {},
    onSuccess: @Composable PagingHandlerScope<T>.() -> Unit,
) {
    val scope = remember(items) { PagingHandlerScope(items) }

    Box(modifier = modifier) {
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
}