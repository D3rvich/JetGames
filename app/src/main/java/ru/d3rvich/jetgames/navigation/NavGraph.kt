package ru.d3rvich.jetgames.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.detail.GameDetailScreen
import ru.d3rvich.filter.FilterScreen
import ru.d3rvich.screenshots.ScreenshotsScreen
import ru.d3rvich.settings.SettingsScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainScreen
    ) {
        addMainScreen(
            navController = navController,
            windowSizeClass = windowSizeClass
        )
        addGameDetailScreen(
            navController = navController,
        )
        addFilterScreen(navController = navController)
        addScreenshotsScreen(
            navController = navController,
        )
        addSettingsScreen(
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.addMainScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
) {
    composable<MainScreen>(popEnterTransition = { EnterTransition.None }) {
        MainScreen(
            externalNavController = navController,
            windowSizeClass = windowSizeClass
        )
    }
}

private fun NavGraphBuilder.addGameDetailScreen(
    navController: NavController,
) {
    composable<Screens.GameDetail> { backStackEntry ->
        var showScreenshots by rememberSaveable {
            mutableStateOf(false)
        }
        GameDetailScreen(
            navigateToScreenshotScreen = { selectedItem, screenshots ->
                val json = Json.encodeToString(
                    Screens.Screenshots(
                        selectedItem,
                        screenshots.map { it.imageUrl })
                )
                backStackEntry.savedStateHandle["Screenshot"] = json
                showScreenshots = true
            },
            navigateBack = { navController.popBackStack() },
        )
        AnimatedVisibility(visible = showScreenshots) {
            val args = backStackEntry.savedStateHandle.get<String>("Screenshot")
                ?.let { argsJson ->
                    Json.decodeFromString<Screens.Screenshots>(argsJson)
                }
            ScreenshotsScreen(
                screenshots = args?.screenshots ?: emptyList(),
                selectedItem = args?.selectedScreenshot ?: 0,
            ) {
                showScreenshots = false
            }
        }
    }
}

private fun NavGraphBuilder.addFilterScreen(navController: NavController) {
    composable<Screens.Filter>(
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        }) {
        FilterScreen(navController = navController)
    }
}

private fun NavGraphBuilder.addScreenshotsScreen(
    navController: NavController,
) {
    composable<Screens.Screenshots> { backStackEntry ->
        val args: Screens.Screenshots = backStackEntry.toRoute()
        ScreenshotsScreen(
            screenshots = args.screenshots.map {
                URLDecoder.decode(it, StandardCharsets.UTF_8.name())
            },
            selectedItem = args.selectedScreenshot,
        ) {
            navController.popBackStack()
        }
    }
}

private fun NavGraphBuilder.addSettingsScreen(navController: NavController) {
    composable<Screens.Settings>(
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        }) {
        SettingsScreen {
            navController.popBackStack()
        }
    }
}

@Serializable
private data object MainScreen
