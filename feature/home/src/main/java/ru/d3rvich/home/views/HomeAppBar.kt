package ru.d3rvich.home.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.common.components.SearchField
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.home.R
import ru.d3rvich.home.model.ListViewMode
import ru.d3rvich.common.R as CommonR
import ru.d3rvich.home.R as HomeR

/**
 * Created by Ilya Deryabin at 16.06.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeAppBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchChange: (String) -> Unit,
    isFilterEdited: Boolean,
    currentListViewMode: ListViewMode,
    onListViewModeChange: (ListViewMode) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateToFilterScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    var showSearch by rememberSaveable {
        mutableStateOf(false)
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    DisposableEffect(showSearch) {
        if (showSearch) {
            focusRequester.requestFocus()
        }
        onDispose {}
    }
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            AnimatedVisibility(visible = !showSearch) {
                IconButton(onClick = {
                    showSearch = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(HomeR.string.open_search)
                    )
                }
            }
        },
        title = {
            AnimatedContent(
                targetState = showSearch,
                label = "Search animation",
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentAlignment = Alignment.Center
            ) {
                if (!it) {
                    Text(text = stringResource(HomeR.string.jetgames))
                } else {
                    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
                        SearchField(
                            text = searchText,
                            modifier = Modifier.fillMaxWidth(),
                            onTextChange = onSearchChange,
                            focusRequester = focusRequester,
                            onClearText = {
                                focusManager.clearFocus()
                                showSearch = false
                            }
                        )
                    }
                }
            }
        },
        actions = {
            BadgedBox(badge = {
                if (isFilterEdited) {
                    Badge(modifier = Modifier.offset(x = (-8).dp, y = 8.dp))
                }
            }) {
                IconButton(
                    onClick = navigateToFilterScreen,
                ) {
                    Icon(
                        painter = painterResource(CommonR.drawable.ic_filter_alt_24),
                        contentDescription = stringResource(HomeR.string.open_filter)
                    )
                }
            }
            var showMenu by rememberSaveable {
                mutableStateOf(false)
            }
            ListViewModeMenu(
                showMenu = showMenu,
                onShowMenuChange = { showMenu = it },
                currentListViewMode = currentListViewMode,
                onListViewModeChange = onListViewModeChange
            )
            IconButton(onClick = navigateToSettingsScreen) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.open_settings)
                )
            }
        })
}

@Composable
private fun ListViewModeMenu(
    modifier: Modifier = Modifier,
    showMenu: Boolean,
    onShowMenuChange: (Boolean) -> Unit,
    currentListViewMode: ListViewMode,
    onListViewModeChange: (ListViewMode) -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.TopEnd) {
        IconButton(onClick = { onShowMenuChange(!showMenu) }) {
            AnimatedContent(
                targetState = currentListViewMode,
                label = "ListViewMode icon animation"
            ) { listViewMode ->
                Icon(
                    painter = painterResource(id = listViewMode.iconResId),
                    contentDescription = listViewMode.name
                )
            }
        }
        DropdownMenu(expanded = showMenu, onDismissRequest = { onShowMenuChange(false) }) {
            ListViewMode.entries.forEach { item ->
                val isSelected = item == currentListViewMode
                val selectedColor = MaterialTheme.colorScheme.primary
                val selectedItemColors = MenuDefaults.itemColors().copy(
                    textColor = selectedColor,
                    leadingIconColor = selectedColor,
                    trailingIconColor = selectedColor
                )
                DropdownMenuItem(
                    colors = if (isSelected) selectedItemColors else MenuDefaults.itemColors(),
                    text = { Text(text = stringResource(id = item.stringResId)) },
                    onClick = {
                        onListViewModeChange(item)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.name
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(visible = isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(HomeR.string.selected)
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun HomeAppBarPreview() {
    JetGamesTheme {
        HomeAppBar(
            searchText = "",
            onSearchChange = {},
            isFilterEdited = false,
            currentListViewMode = ListViewMode.Compact,
            onListViewModeChange = {},
            navigateToFilterScreen = {},
            navigateToSettingsScreen = {}
        )
    }
}