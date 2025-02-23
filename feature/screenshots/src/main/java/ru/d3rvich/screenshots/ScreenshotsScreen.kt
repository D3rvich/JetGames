package ru.d3rvich.screenshots

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.util.lerp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.core.ui.utils.findActivity
import ru.d3rvich.screenshots.model.draggableScreenshot
import ru.d3rvich.screenshots.model.rememberDragToDismissState
import ru.d3rvich.screenshots.views.PageIndicator
import ru.d3rvich.screenshots.views.ScreenshotView
import kotlin.math.absoluteValue

/**
 * Created by Ilya Deryabin at 12.04.2024
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScreenshotsScreen(
    modifier: Modifier = Modifier,
    screenshots: List<String>,
    selectedItem: Int = 0,
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    var showWidgets: Boolean by rememberSaveable {
        mutableStateOf(true)
    }
    BackHandler {
        onBackPressed()
    }
    SystemBarsController(showSystemBars = showWidgets)
    JetGamesTheme(dynamicColor = false, darkTheme = true) {
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            BoxWithConstraints {
                val heightToDismiss = with(LocalDensity.current) {
                    maxHeight.toPx() / 6
                }
                val dragState = rememberDragToDismissState(heightToDismiss = heightToDismiss)
                Surface(
                    modifier = modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background.copy(
                        alpha = lerp(1f, 0.5f, dragState.fraction)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .draggableScreenshot(
                                dragToDismissState = dragState,
                                onHeightOffsetChange = {
                                    showWidgets = false
                                },
                                onDismissRequest = onBackPressed
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                showWidgets = !showWidgets
                            }
                    ) {
                        val pagerState =
                            rememberPagerState(
                                initialPage = selectedItem,
                                pageCount = { screenshots.size })
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize(),
                            state = pagerState
                        ) { page ->
                            val screenshot = screenshots[page]
                            ScreenshotView(
                                screenshot = screenshot,
                                pageOffset = ((pagerState.currentPage - page) +
                                        pagerState.currentPageOffsetFraction)
                                    .absoluteValue,
                                dragToDismissState = dragState,
                                windowSizeClass = windowSizeClass,
                                onHeightOffsetChange = {
                                    showWidgets = false
                                },
                                onDismissRequest = {
                                    onBackPressed()
                                }
                            )
                        }
                        AnimateWidgetVisibility(
                            visible = showWidgets,
                            direction = AnimationDirection.Up,
                            modifier = Modifier.align(Alignment.TopCenter),
                        ) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors()
                                    .copy(containerColor = Color.Transparent),
                                title = { },
                                navigationIcon = {
                                    IconButton(onClick = onBackPressed) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                            contentDescription = "Navigate back"
                                        )
                                    }
                                }
                            )
                        }
                        AnimateWidgetVisibility(
                            visible = showWidgets,
                            direction = AnimationDirection.Down,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            PageIndicator(
                                pageCount = pagerState.pageCount,
                                currentPageIndex = pagerState.currentPage
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimateWidgetVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    direction: AnimationDirection,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically {
            when (direction) {
                AnimationDirection.Up -> -it / 2
                AnimationDirection.Down -> it / 2
            }
        },
        exit = fadeOut() + slideOutVertically {
            when (direction) {
                AnimationDirection.Up -> -it / 2
                AnimationDirection.Down -> it / 2
            }
        },
        modifier = modifier
    ) {
        content()
    }
}

private enum class AnimationDirection {
    Up,
    Down
}

@SuppressLint("WrongConstant")
@Composable
private fun SystemBarsController(showSystemBars: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val window = context.findActivity().window
    val insetsController =
        window?.let { WindowCompat.getInsetsController(window, window.decorView) }
    val darkTheme = isSystemInDarkTheme()
    DisposableEffect(showSystemBars) {
        if (!showSystemBars) {
            insetsController?.apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        scope.launch {
            delay(300)
            if (!showSystemBars) {
                insetsController?.apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }
        onDispose {
            insetsController?.apply {
                show(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
}