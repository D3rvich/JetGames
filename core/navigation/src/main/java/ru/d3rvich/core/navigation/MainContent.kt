package ru.d3rvich.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.d3rvich.core.navigation.main.MainComponent
import ru.d3rvich.feature.browse.ui.BrowseContent
import ru.d3rvich.feature.favorites.ui.FavoritesContent
import ru.d3rvich.feature.home.ui.HomeContent

@Composable
fun MainContent(
    mainComponent: MainComponent,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val showNavRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val showBottomBar = !showNavRail
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = if (showNavRail) {
            WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        } else {
            WindowInsets(0)
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(mainComponent)
            }
        }) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            if (showNavRail) {
                NavRail(mainComponent)
            }
            Children(mainComponent.stack, Modifier.fillMaxSize()) {
                when (val child = it.instance) {
                    is MainComponent.Child.Browse -> {
                        BrowseContent(component = child.component, contentPadding = paddingValues)
                    }

                    is MainComponent.Child.Favorites -> {
                        FavoritesContent(
                            component = child.component,
                            contentPadding = paddingValues,
                        )
                    }

                    is MainComponent.Child.Home -> HomeContent(
                        component = child.component,
                        contentPadding = paddingValues,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(mainComponent: MainComponent, modifier: Modifier = Modifier) {
    val stack by mainComponent.stack.subscribeAsState()
    val currentTabItem = remember(stack) { stack.active.instance.toTabItem() }
    NavigationBar(
        modifier = modifier,
        containerColor = NavigationBarDefaults.containerColor.copy(alpha = 0.9f)
    ) {
        TabItems.entries.forEach { tab ->
            val isSelected = currentTabItem == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    mainComponent.performClick(tab)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) tab.selectedIconResId else tab.unselectedIconResId
                        ),
                        contentDescription = stringResource(tab.labelResId)
                    )
                },
                label = { Text(text = stringResource(id = tab.labelResId)) }
            )
        }
    }
}

@Composable
private fun NavRail(mainComponent: MainComponent, modifier: Modifier = Modifier) {
    val stack by mainComponent.stack.subscribeAsState()
    val currentTabItem = remember(stack) { stack.active.instance.toTabItem() }
    NavigationRail(modifier = modifier) {
        TabItems.entries.forEach { tab ->
            val isSelected = currentTabItem == tab
            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    mainComponent.performClick(tab)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) tab.selectedIconResId
                            else tab.unselectedIconResId
                        ),
                        contentDescription = stringResource(tab.labelResId)
                    )
                },
                label = { Text(text = stringResource(id = tab.labelResId)) }
            )
        }
    }
}