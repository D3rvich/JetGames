package ru.d3rvich.feature.home.ui.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.util.lerp
import ru.d3rvich.common.components.SearchField
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.home.ui.model.iconResId
import ru.d3rvich.feature.home.ui.model.stringResId
import ru.d3rvich.common.R as CommonR
import ru.d3rvich.feature.home.ui.R as HomeR

/**
 * Created by Ilya Deryabin at 16.06.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeAppBar(
    searchText: String,
    isFilterEdited: Boolean,
    currentListDisplayOption: ListDisplayOption,
    onSearchChange: (String) -> Unit,
    onListDisplayOptionChange: (ListDisplayOption) -> Unit,
    navigateToFilterScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    var showSearch by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(showSearch) {
        if (showSearch) {
            focusRequester.requestFocus()
        }
    }
    val alphaFraction =
        lerp(1f, 0.5f, scrollBehavior?.state?.collapsedFraction ?: 0f)
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(
                scrolledContainerColor =
                    TopAppBarDefaults.topAppBarColors().scrolledContainerColor.copy(alpha = alphaFraction)
            ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            AnimatedVisibility(visible = !showSearch) {
                IconButton(onClick = {
                    showSearch = true
                }) {
                    Icon(
                        painter = painterResource(CommonR.drawable.search_24px),
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
                currentListDisplayOption = currentListDisplayOption,
                onListViewModeChange = onListDisplayOptionChange
            )
            IconButton(onClick = navigateToSettingsScreen) {
                Icon(
                    painter = painterResource(CommonR.drawable.settings_24px),
                    contentDescription = stringResource(HomeR.string.open_settings)
                )
            }
        })
}

@Composable
private fun ListViewModeMenu(
    showMenu: Boolean,
    currentListDisplayOption: ListDisplayOption,
    onShowMenuChange: (Boolean) -> Unit,
    onListViewModeChange: (ListDisplayOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.TopEnd) {
        IconButton(onClick = { onShowMenuChange(!showMenu) }) {
            AnimatedContent(
                targetState = currentListDisplayOption,
                label = "ListViewOption icon animation"
            ) { listDisplayOption ->
                Icon(
                    painter = painterResource(id = listDisplayOption.iconResId),
                    contentDescription = listDisplayOption.name
                )
            }
        }
        DropdownMenu(expanded = showMenu, onDismissRequest = { onShowMenuChange(false) }) {
            ListDisplayOption.entries.forEach { item ->
                val isSelected = item == currentListDisplayOption
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
                                painter = painterResource(CommonR.drawable.check_24px),
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
            currentListDisplayOption = ListDisplayOption.Compact,
            onListDisplayOptionChange = {},
            navigateToFilterScreen = {},
            navigateToSettingsScreen = {}
        )
    }
}