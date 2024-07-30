package ru.d3rvich.common.components.collapsing_appbar

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Created by Ilya Deryabin at 08.03.2024
 */
@Composable
fun CollapsingTopAppBar(
    modifier: Modifier = Modifier,
    collapsingTitle: CollapsingTitle? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    expandedBackground: (@Composable () -> Unit)? = null,
    scrollBehavior: CollapsingTopAppBarScrollBehavior? = null,
    collapsedElevation: Dp = DefaultCollapsedElevation,
    enableStatusBarBackground: Boolean = true,
    enableTitleGradientBackground: Boolean = collapsingTitle != null && expandedBackground != null,
    windowInsets: WindowInsets = DefaultWindowInsets,
    colors: CollapsingTopAppBarColors = CollapsingTopAppBarDefaults.colors,
) {
    val collapsedFraction = when {
        scrollBehavior != null -> scrollBehavior.state.collapsedFraction
        else -> 1f
    }

    val fullyCollapsedTitleScale = when {
        collapsingTitle != null -> CollapsedTitleLineHeight.value / collapsingTitle.expandedTextStyle.lineHeight.value
        else -> 1f
    }

    val collapsingTitleScale = lerp(1f, fullyCollapsedTitleScale, collapsedFraction)

    val showElevation = when {
        scrollBehavior == null -> false
        scrollBehavior.state.contentOffset <= 0 && collapsedFraction == 1f -> true
        scrollBehavior.state.contentOffset < -1f -> true
        else -> false
    }

    val elevationState =
        animateDpAsState(if (showElevation) collapsedElevation else 0.dp, label = "elevationState")

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val dragModifier = if (scrollBehavior != null) {
        Modifier.draggable(orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset += delta
            },
            onDragStopped = { velocity ->
                settleAppBar(
                    state = scrollBehavior.state,
                    velocity = velocity,
                    flingAnimationSpec = scrollBehavior.flingAnimationSpec
                )
            }
        )
    } else {
        Modifier
    }

    Surface(
        modifier = modifier,
        shadowElevation = elevationState.value,
        color = colors.containerColor
    ) {
        Layout(
            content = {
                if (expandedBackground != null) {
                    Box(
                        modifier = Modifier
                            .layoutId(BackgroundId)
                            .wrapContentSize()
                    ) {
                        expandedBackground()
                    }
                }
                if (enableStatusBarBackground) {
                    val color = if (isSystemInDarkTheme()) Color.Black else Color.White
                    Box(
                        modifier = Modifier
                            .layoutId(StatusBarGradientId)
                            .background(color.copy(alpha = 0.5f))
                            .fillMaxSize()
                    )
                }
                if (enableTitleGradientBackground) {
                    val gradientBrush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        )
                    )
                    Box(
                        modifier = Modifier
                            .layoutId(TitleGradientBackgroundId)
                            .background(gradientBrush)
                            .fillMaxSize()
                    )
                }
                if (collapsingTitle != null) {
                    CompositionLocalProvider(LocalContentColor provides colors.titleContentColor) {
                        Text(
                            text = collapsingTitle.titleText,
                            style = collapsingTitle.expandedTextStyle,
                            modifier = Modifier
                                .layoutId(ExpandedTitleId)
                                .wrapContentHeight(align = Alignment.Top)
                                .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Top))
                                .graphicsLayer(
                                    scaleX = collapsingTitleScale,
                                    scaleY = collapsingTitleScale,
                                    transformOrigin = TransformOrigin(0f, 0f)
                                )
                        )
                        Text(
                            modifier = Modifier
                                .layoutId(CollapsedTitleId)
                                .wrapContentHeight(align = Alignment.Top)
                                .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Top))
                                .graphicsLayer(
                                    scaleX = collapsingTitleScale,
                                    scaleY = collapsingTitleScale,
                                    transformOrigin = TransformOrigin(0f, 0f)
                                ),
                            text = collapsingTitle.titleText,
                            style = collapsingTitle.expandedTextStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (navigationIcon != null) {
                    CompositionLocalProvider(LocalContentColor provides colors.navigationIconContentColor) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Top + WindowInsetsSides.Start))
                                .layoutId(NavigationIconId)
                        ) {
                            navigationIcon()
                        }
                    }
                }

                if (actions != null) {
                    CompositionLocalProvider(LocalContentColor provides colors.actionsContentColor) {
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Top + WindowInsetsSides.End))
                                .layoutId(ActionsId)
                        ) {
                            actions()
                        }
                    }
                }
            },
            modifier = modifier.then(dragModifier.heightIn(min = MinCollapsedHeight))
        ) { measurables, constraints ->
            val statusBarHeightPx = windowInsets.getTop(density).toDp().toPx()
            val horizontalPaddingPx = HorizontalPadding.toPx()
            val expandedTitleBottomPaddingPx = ExpandedTitleBottomPadding.toPx()
            val startWindowInsetsPaddingPx = windowInsets.getLeft(density, layoutDirection).toDp().toPx()
            val endWindowInsetsPaddingPx = windowInsets.getRight(density, layoutDirection).toDp().toPx()

            // Measuring widgets inside toolbar:

            val backgroundPlaceable =
                measurables.firstOrNull { it.layoutId == BackgroundId }
                    ?.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth,
                            minHeight = 0,
                            minWidth = 0
                        )
                    )

            val statusBarGradientPlaceable =
                measurables.firstOrNull { it.layoutId == StatusBarGradientId }?.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth,
                        maxHeight = statusBarHeightPx.roundToInt(),
                        minHeight = 0,
                    )
                )

            val navigationIconPlaceable =
                measurables.firstOrNull { it.layoutId == NavigationIconId }
                    ?.measure(constraints.copy(minWidth = 0))

            val actionsPlaceable = measurables.firstOrNull { it.layoutId == ActionsId }
                ?.measure(constraints.copy(minWidth = 0))

            val expandedTitlePlaceable =
                measurables.firstOrNull { it.layoutId == ExpandedTitleId }
                    ?.measure(
                        constraints.copy(
                            maxWidth = (constraints.maxWidth - 2 * horizontalPaddingPx).roundToInt(),
                            minWidth = 0,
                            minHeight = 0
                        )
                    )

            val titleGradientBackgroundPlaceable =
                measurables.firstOrNull { it.layoutId == TitleGradientBackgroundId }
                    ?.measure(
                        constraints.copy(
                            minHeight = 0,
                            maxHeight = (expandedTitlePlaceable?.height
                                ?: 0) + 2 * expandedTitleBottomPaddingPx.roundToInt(),
                            maxWidth = constraints.maxWidth
                        )
                    )

            val navigationIconOffset = when (navigationIconPlaceable) {
                null -> horizontalPaddingPx + startWindowInsetsPaddingPx
                else -> navigationIconPlaceable.width + horizontalPaddingPx * 2
            }

            val actionsOffset = when (actionsPlaceable) {
                null -> horizontalPaddingPx + endWindowInsetsPaddingPx
                else -> actionsPlaceable.width + horizontalPaddingPx * 2
            }

            val collapsedTitleMaxWidthPx =
                (constraints.maxWidth - navigationIconOffset - actionsOffset) / fullyCollapsedTitleScale

            val collapsedTitlePlaceable =
                measurables.firstOrNull { it.layoutId == CollapsedTitleId }
                    ?.measure(
                        constraints.copy(
                            maxWidth = collapsedTitleMaxWidthPx.roundToInt(),
                            minWidth = 0,
                            minHeight = 0
                        )
                    )

            val collapsedHeightPx = MinCollapsedHeight.toPx() + statusBarHeightPx

            var layoutHeightPx = collapsedHeightPx

            // Calculating coordinates of widgets inside toolbar:

            // Current coordinates of navigation icon
            val navigationIconX = horizontalPaddingPx.roundToInt()
            val navigationIconY =
                ((collapsedHeightPx - (navigationIconPlaceable?.height ?: 0)) / 2).roundToInt()

            // Current coordinates of actions
            val actionsX =
                (constraints.maxWidth - (actionsPlaceable?.width
                    ?: 0) - horizontalPaddingPx).roundToInt()
            val actionsY =
                ((collapsedHeightPx - (actionsPlaceable?.height ?: 0)) / 2).roundToInt()

            // Current coordinates of title
            var collapsingTitleY = 0
            var collapsingTitleX = 0
            var titleGradientBackgroundY = 0

            val backgroundHeightPx = backgroundPlaceable?.height?.toDp()?.toPx()
            if (expandedTitlePlaceable != null && collapsedTitlePlaceable != null) {
                // Measuring toolbar collapsing distance
                val heightOffsetLimitPx =
                    (expandedTitlePlaceable.height + expandedTitleBottomPaddingPx + statusBarHeightPx).coerceAtLeast(
                        (backgroundHeightPx ?: 0f) - MinCollapsedHeight.toPx()
                    )
                scrollBehavior?.state?.heightOffsetLimit = -heightOffsetLimitPx

                // Toolbar height at fully expanded state
                val fullyExpandedHeightPx = MinCollapsedHeight.toPx() + heightOffsetLimitPx

                // Coordinates of fully expanded title
                val fullyExpandedTitleX = startWindowInsetsPaddingPx + 2 * horizontalPaddingPx
                val fullyExpandedTitleY =
                    fullyExpandedHeightPx - expandedTitlePlaceable.height - expandedTitleBottomPaddingPx
                titleGradientBackgroundY =
                    (fullyExpandedTitleY - expandedTitleBottomPaddingPx).roundToInt()

                // Coordinates of fully collapsed title
                val fullyCollapsedTitleX = navigationIconOffset
                val fullyCollapsedTitleY =
                    collapsedHeightPx / 2 - CollapsedTitleLineHeight.toPx()

                // Current height of toolbar
                layoutHeightPx =
                    lerp(fullyExpandedHeightPx, collapsedHeightPx, collapsedFraction)

                // Current coordinates of collapsing title
                collapsingTitleX =
                    lerp(
                        fullyExpandedTitleX,
                        fullyCollapsedTitleX,
                        collapsedFraction
                    ).roundToInt()
                collapsingTitleY =
                    lerp(
                        fullyExpandedTitleY,
                        fullyCollapsedTitleY,
                        collapsedFraction
                    ).roundToInt()
            } else if (backgroundHeightPx != null && backgroundHeightPx > collapsedHeightPx) {
                scrollBehavior?.state?.heightOffsetLimit = backgroundHeightPx
                layoutHeightPx = lerp(backgroundHeightPx, collapsedHeightPx, collapsedFraction)
            } else {
                scrollBehavior?.state?.heightOffsetLimit = -1f
            }

            val toolbarHeightPx = layoutHeightPx.roundToInt()

            // Placing toolbar widgets:

            layout(constraints.maxWidth, toolbarHeightPx) {
                backgroundPlaceable?.placeRelativeWithLayer(
                    x = 0,
                    y = 0,
                    zIndex = -1f,
                    layerBlock = {
                        alpha = 1 - collapsedFraction
                    }
                )
                statusBarGradientPlaceable?.placeRelativeWithLayer(
                    x = 0,
                    y = 0,
                    layerBlock = { alpha = 1 - collapsedFraction })
                titleGradientBackgroundPlaceable?.placeRelative(
                    x = 0,
                    y = titleGradientBackgroundY,
                    zIndex = -1f
                )
                navigationIconPlaceable?.placeRelative(
                    x = navigationIconX,
                    y = navigationIconY
                )
                actionsPlaceable?.placeRelative(
                    x = actionsX,
                    y = actionsY
                )
                if (expandedTitlePlaceable?.width == collapsedTitlePlaceable?.width) {
                    expandedTitlePlaceable?.placeRelative(
                        x = collapsingTitleX,
                        y = collapsingTitleY,
                    )
                } else {
                    expandedTitlePlaceable?.placeRelativeWithLayer(
                        x = collapsingTitleX,
                        y = collapsingTitleY,
                        layerBlock = { alpha = 1 - collapsedFraction }
                    )
                    collapsedTitlePlaceable?.placeRelativeWithLayer(
                        x = collapsingTitleX,
                        y = collapsingTitleY,
                        layerBlock = { alpha = collapsedFraction }
                    )
                }
            }
        }
    }
}

private val MinCollapsedHeight = 64.dp
private val HorizontalPadding = 4.dp
private val ExpandedTitleBottomPadding = 8.dp
private val CollapsedTitleLineHeight = 28.sp
private val DefaultCollapsedElevation = 4.dp

@OptIn(ExperimentalLayoutApi::class)
private val DefaultWindowInsets
    @Composable
    get() = WindowInsets.systemBarsIgnoringVisibility.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)

private const val ExpandedTitleId = "expandedTitle"
private const val CollapsedTitleId = "collapsedTitle"
private const val NavigationIconId = "navigationIcon"
private const val ActionsId = "actions"
private const val BackgroundId = "background"
private const val StatusBarGradientId = "statusBarGradient"
private const val TitleGradientBackgroundId = "titleGradientBackground"

private suspend fun settleAppBar(
    state: CollapsingTopAppBarScrollState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
): Velocity {
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    if (flingAnimationSpec != null && abs(velocity) > 1F) {
        var lastValue = 0f
        AnimationState(initialValue = 0f, initialVelocity = velocity)
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    snapAppBar(state)
    return Velocity(0f, remainingVelocity)
}

object CollapsingTopAppBarDefaults {

    @Stable
    val colors
        @Composable get() = CollapsingTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
            titleContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
            actionsContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface)
        )
}

data class CollapsingTopAppBarColors(
    val containerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionsContentColor: Color,
)

@Stable
data class CollapsingTitle(
    val titleText: String,
    val expandedTextStyle: TextStyle,
) {

    companion object {
        @Composable
        fun large(titleText: String) =
            CollapsingTitle(titleText, MaterialTheme.typography.headlineLarge)

        @Composable
        fun medium(titleText: String) =
            CollapsingTitle(titleText, MaterialTheme.typography.headlineMedium)
    }
}