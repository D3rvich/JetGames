package ru.d3rvich.jetgames.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.core.domain.entities.ScreenshotEntity
import ru.d3rvich.detail.GameDetailScreen
import ru.d3rvich.filter.FilterScreen
import ru.d3rvich.screenshots.ScreenshotsScreen
import ru.d3rvich.settings.SettingsScreen

@Composable
fun SetupNavGraph(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(MainScreen)
    val commonEntryDecorators: List<NavEntryDecorator<NavKey>> = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )
    val overlaySceneStrategy = rememberOverlaySceneStrategy<NavKey>()
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = commonEntryDecorators,
        sceneStrategies = listOf(overlaySceneStrategy),
        transitionSpec = { fadeIn() togetherWith ExitTransition.KeepUntilTransitionsFinished },
        popTransitionSpec = { EnterTransition.None togetherWith fadeOut() },
        predictivePopTransitionSpec = { EnterTransition.None togetherWith fadeOut() },
        entryProvider = entryProvider {
            addMainScreen(
                backStack = backStack,
                windowSizeClass = windowSizeClass,
                entryDecorators = commonEntryDecorators
            )
            addGameDetailScreen(navigateToScreenshots = {
                backStack.add(it)
            }, navigateBack = { backStack.removeLastOrNull() })
            addScreenshotsScreen { backStack.removeLastOrNull() }
            addFilterScreen { backStack.removeLastOrNull() }
            addSettingsScreen { backStack.removeLastOrNull() }
        })
}

private fun EntryProviderScope<NavKey>.addMainScreen(
    backStack: NavBackStack<NavKey>,
    windowSizeClass: WindowSizeClass,
    entryDecorators: List<NavEntryDecorator<NavKey>> = emptyList()
) {
    entry<MainScreen> {
        MainScreen(
            externalBackStack = backStack,
            windowSizeClass = windowSizeClass,
            entryDecorators = entryDecorators
        )
    }
}

private fun EntryProviderScope<NavKey>.addGameDetailScreen(
    navigateToScreenshots: (Screens.Screenshots) -> Unit,
    navigateBack: () -> Unit
) {
    entry<Screens.GameDetail>(
        metadata = OverlayScene.hostKey()
    ) { gameDetail ->
        GameDetailScreen(
            gameId = gameDetail.gameId,
            navigateToScreenshotScreen = { selected: Int, list: List<ScreenshotEntity> ->
                val screen = Screens.Screenshots(selected, list.map { it.imageUrl })
                navigateToScreenshots(screen)
            },
            navigateBack = navigateBack
        )
    }
}

private fun EntryProviderScope<NavKey>.addScreenshotsScreen(navigateBack: () -> Unit) {
    entry<Screens.Screenshots>(
        metadata = OverlayScene.overlayKey()
    ) {
        ScreenshotsScreen(
            screenshots = it.screenshots,
            selectedItem = it.selectedScreenshot,
            onBackPressed = navigateBack
        )
    }
}

private fun EntryProviderScope<NavKey>.addFilterScreen(navigateBack: () -> Unit) {
    entry<Screens.Filter>(metadata = NavDisplay.transitionSpec {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) togetherWith
                ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        EnterTransition.None togetherWith
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    } + NavDisplay.predictivePopTransitionSpec {
        EnterTransition.None togetherWith
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    }) {
        FilterScreen(navigateBack = navigateBack)
    }
}

private fun EntryProviderScope<NavKey>.addSettingsScreen(navigateBack: () -> Unit) {
    entry<Screens.Settings>(metadata = NavDisplay.transitionSpec {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) togetherWith
                ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        EnterTransition.None togetherWith
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    } + NavDisplay.predictivePopTransitionSpec {
        EnterTransition.None togetherWith
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    }
    ) {
        SettingsScreen(navigateBack = navigateBack)
    }
}

@Serializable
private data object MainScreen : NavKey
