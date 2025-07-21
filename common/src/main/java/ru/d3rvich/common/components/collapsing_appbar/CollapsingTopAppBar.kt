package ru.d3rvich.common.components.collapsing_appbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirst
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedContent: @Composable () -> Unit = {},
    collapsedHeight: Dp = CollapsedHeight,
    expandedHeight: Dp = ExpandedHeight,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    windowInsets: WindowInsets = DefaultWindowInsets,
    colors: CollapsingTopAppBarColors = CollapsingTopAppBarDefaults.colors,
) {
    require(expandedHeight >= collapsedHeight) {
        "The expandedHeight is expected to be greater or equal to the collapsedHeight"
    }
    val collapsedHeightPx: Float
    val expandedHeightPx: Float
    val topWindowInsetsPx: Float
    with(LocalDensity.current) {
        collapsedHeightPx = collapsedHeight.toPx()
        expandedHeightPx = expandedHeight.toPx()
        topWindowInsetsPx = windowInsets.getTop(this).toFloat()
    }
    SideEffect {
        val heightOffsetLimit = collapsedHeightPx - expandedHeightPx + topWindowInsetsPx
        if (scrollBehavior != null && scrollBehavior.state.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior.state.heightOffsetLimit = heightOffsetLimit
        }
    }
    val collapsedFraction = scrollBehavior?.state?.collapsedFraction ?: 1f

    val colorTransitionFraction = scrollBehavior?.state?.collapsedFraction ?: 0f
    val appBarContainerColor = colors.containerColor(colorTransitionFraction)

    val hideTopTitleSemantics = colorTransitionFraction < 0.5f
    val hideBottomTitleSemantics = !hideTopTitleSemantics

    val actionsRaw = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            content = actions
        )
    }
    val titleAlpha = TopTitleAlphaEasing.transform(colorTransitionFraction)

    val dragModifier = if (scrollBehavior != null) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset += delta
            },
            onDragStopped = { velocity ->
                settleAppBar(
                    state = scrollBehavior.state,
                    velocity = velocity,
                    flingAnimationSpec = scrollBehavior.flingAnimationSpec,
                    snapAnimationSpec = scrollBehavior.snapAnimationSpec
                )
            }
        )
    } else {
        Modifier
    }

    Surface(modifier = modifier.then(dragModifier), color = appBarContainerColor) {
        Box {
            ExpandedContentLayout(
                modifier = Modifier
                    .clipToBounds()
                    .heightIn(max = expandedHeight)
                    .graphicsLayer {
                        alpha = 1 - collapsedFraction
                    },
                content = expandedContent,
                scrolledOffset = { scrollBehavior?.state?.heightOffset ?: 0f }
            )
            TopAppBarLayout(
                modifier = Modifier
                    .windowInsetsPadding(windowInsets)
                    .clipToBounds()
                    .heightIn(max = collapsedHeight),
                navigationIconContentColor = colors.navigationIconContentColor,
                titleContentColor = colors.titleContentColor,
                actionIconContentColor = colors.actionsContentColor,
                title = title,
                titleTextStyle = titleTextStyle,
                titleAlpha = titleAlpha,
                hideTitleSemantics = hideBottomTitleSemantics,
                navigationIcon = navigationIcon,
                actions = actionsRaw
            )
        }
    }
}

@Composable
private fun TopAppBarLayout(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    titleContentColor: Color,
    navigationIconContentColor: Color,
    actionIconContentColor: Color,
    titleAlpha: Float,
    titleTextStyle: TextStyle,
    hideTitleSemantics: Boolean,
    modifier: Modifier = Modifier,
) {
    Layout(
        content = {
            Box(
                Modifier
                    .layoutId(NavigationIconId)
                    .padding(start = HorizontalPadding)
                    .wrapContentSize()
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides navigationIconContentColor,
                    content = navigationIcon
                )
            }
            Box(
                modifier = Modifier
                    .layoutId(TitleId)
                    .padding(horizontal = HorizontalPadding)
                    .then(if (hideTitleSemantics) Modifier.clearAndSetSemantics {} else Modifier)
                    .graphicsLayer(alpha = titleAlpha)
            ) {
                val mergedStyle = LocalTextStyle.current.merge(titleTextStyle)
                CompositionLocalProvider(
                    LocalTextStyle provides mergedStyle,
                    LocalContentColor provides titleContentColor,
                    content = title
                )
            }
            Box(
                Modifier
                    .layoutId(ActionsId)
                    .padding(end = HorizontalPadding)
                    .wrapContentSize()
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides actionIconContentColor,
                    content = actions
                )
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val navigationIconPlaceable = measurables.fastFirst { it.layoutId == NavigationIconId }
            .measure(constraints.copy(minWidth = 0))
        val actionsPlaceable = measurables.fastFirst { it.layoutId == ActionsId }
            .measure(constraints.copy(minWidth = 0))
        val titleMaxWidth = if (constraints.maxWidth == Constraints.Infinity) {
            constraints.maxWidth
        } else {
            (constraints.maxWidth - navigationIconPlaceable.width - actionsPlaceable.width).coerceAtLeast(
                0
            )
        }
        val titlePlaceable = measurables.fastFirst { it.layoutId == TitleId }
            .measure(constraints.copy(minWidth = 0, maxWidth = titleMaxWidth))

        val layoutHeight = constraints.maxHeight

        layout(constraints.maxWidth, layoutHeight) {

            navigationIconPlaceable.placeRelative(
                x = 0,
                y = (layoutHeight - navigationIconPlaceable.height) / 2
            )

            titlePlaceable.placeRelative(
                x = max(TopAppBarTitleInset.roundToPx(), navigationIconPlaceable.width),
                y = (layoutHeight - titlePlaceable.height) / 2
            )

            actionsPlaceable.placeRelative(
                x = constraints.maxWidth - actionsPlaceable.width,
                y = (layoutHeight - actionsPlaceable.height) / 2
            )
        }
    }
}

@Composable
private fun ExpandedContentLayout(
    scrolledOffset: ScrolledOffset,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = {
            Box(
                Modifier
                    .layoutId(ExpandedContentId)
                    .wrapContentSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                content()
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val scrolledOffsetValue = scrolledOffset.offset()
        val heightOffset = if (scrolledOffsetValue.isNaN()) 0 else scrolledOffsetValue.roundToInt()

        val layoutHeight =
            if (constraints.maxHeight == Constraints.Infinity) {
                constraints.maxHeight
            } else {
                constraints.maxHeight + heightOffset
            }

        val contentPlaceable = measurables.fastFirst { it.layoutId == ExpandedContentId }
            .measure(constraints.copy(minHeight = 0, maxHeight = layoutHeight))

        layout(constraints.maxWidth, layoutHeight) {
            contentPlaceable.placeRelative(x = 0, y = 0)
        }
    }
}

private val CollapsedHeight = 64.dp
private val ExpandedHeight = 240.dp
private val HorizontalPadding = 4.dp
private val TopAppBarTitleInset = 16.dp - HorizontalPadding

private val DefaultWindowInsets
    @Composable get() = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

private const val TitleId = "title"
private const val NavigationIconId = "navigationIcon"
private const val ActionsId = "actions"
private const val ExpandedContentId = "expandedContent"

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
) {
    // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
    // and just return Zero Velocity.
    // Note that we don't check for 0f due to float precision with the collapsedFraction
    // calculation.
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return
    }
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 && state.heightOffset > state.heightOffsetLimit) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) {
                state.heightOffset = value
            }
        }
    }
}

@Stable
class CollapsingTopAppBarColors(
    val containerColor: Color,
    val scrolledContainerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionsContentColor: Color,
) {
    @Stable
    internal fun containerColor(colorTransitionFraction: Float): Color = lerp(
        containerColor,
        scrolledContainerColor,
        FastOutLinearInEasing.transform(colorTransitionFraction)
    )
}

object CollapsingTopAppBarDefaults {
    @Stable
    val colors
        @Composable get() = CollapsingTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
            titleContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
            actionsContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface)
        )
}

private val TopTitleAlphaEasing = CubicBezierEasing(.8f, 0f, .8f, .15f)

private fun interface ScrolledOffset {
    fun offset(): Float
}