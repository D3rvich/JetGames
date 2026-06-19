package ru.d3rvich.feature.screenshots

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.util.lerp
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.domain.model.ColorModeType
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.ui.model.asUiState
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.screenshots.util.DragToDismissState
import ru.d3rvich.feature.screenshots.util.draggableScreenshot
import ru.d3rvich.feature.screenshots.util.rememberDragToDismissState
import ru.d3rvich.feature.screenshots.util.SystemBarsController
import ru.d3rvich.feature.screenshots.views.PageIndicator
import ru.d3rvich.feature.screenshots.views.ScreenshotView
import kotlin.math.abs
import ru.d3rvich.common.R as uiR

/**
 * Created by Ilya Deryabin at 12.04.2024
 */
@Composable
fun ScreenshotsScreen(
    screenshots: ImmutableList<String>,
    modifier: Modifier = Modifier,
    selectedItem: Int = 0,
    onPageChange: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    var showWidgets: Boolean by rememberSaveable {
        mutableStateOf(true)
    }
    BackHandler {
        onBackPressed()
    }
    SystemBarsController(showSystemBars = showWidgets)
    ScreenWrapper {
        val maxHeight = maxHeight
        val heightToDismiss = with(LocalDensity.current) {
            maxHeight.toPx() / 6
        }
        val dragState = rememberDragToDismissState(heightToDismiss = heightToDismiss)
        val backgroundColor = MaterialTheme.colorScheme.background
        Surface(
            modifier = modifier
                .fillMaxSize()
                .drawBehind {
                    val alpha = lerp(1f, 0.5f, dragState.fraction)
                    drawRect(color = backgroundColor.copy(alpha = alpha))
                },
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .draggableScreenshot(
                        dragToDismissState = dragState,
                        onHeightOffsetChange = { showWidgets = false },
                        onDismissRequest = onBackPressed
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showWidgets = !showWidgets
                    }
            ) {
                val pagerState = rememberPagerState(
                    initialPage = selectedItem,
                    pageCount = { screenshots.size })
                PhotoPager(
                    pagerState = pagerState,
                    dragState = dragState,
                    item = screenshots::get,
                    onPageChange = onPageChange
                )
                TopBarWidget(
                    showWidget = showWidgets,
                    onBackPressed = onBackPressed,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                PageIndicatorWidget(
                    showWidget = showWidgets,
                    currentPage = pagerState.currentPage,
                    pageCount = pagerState.pageCount,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun ScreenWrapper(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    val darkPreferences = UserPreferences(ThemeType.Dark, ColorModeType.Default).asUiState()
    JetGamesTheme(darkPreferences) {
        CompositionLocalProvider(LocalOverscrollFactory provides null) {
            BoxWithConstraints(modifier = modifier) {
                content()
            }
        }
    }
}

@Composable
private fun PhotoPager(
    pagerState: PagerState,
    dragState: DragToDismissState,
    item: (page: Int) -> String,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pagerState.currentPage)
    }
    HorizontalPager(
        modifier = modifier.fillMaxSize(),
        state = pagerState
    ) { page ->
        ScreenshotView(
            screenshot = item(page),
            pageOffset = {
                abs(pagerState.currentPage - page + pagerState.currentPageOffsetFraction)
            },
            dragToDismissState = dragState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarWidget(
    showWidget: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    AnimateWidgetVisibility(
        visible = showWidget,
        direction = AnimationDirection.Up,
        modifier = modifier,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors()
                .copy(containerColor = Color.Transparent),
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        painter = painterResource(uiR.drawable.arrow_back_24px),
                        contentDescription = "Navigate back"
                    )
                }
            }
        )
    }
}

@Composable
private fun PageIndicatorWidget(
    showWidget: Boolean,
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    AnimateWidgetVisibility(
        visible = showWidget,
        direction = AnimationDirection.Down,
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        PageIndicator(
            pageCount = pageCount,
            currentPageIndex = currentPage
        )
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