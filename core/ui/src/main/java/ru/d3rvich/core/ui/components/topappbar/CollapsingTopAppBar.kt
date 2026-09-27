package ru.d3rvich.core.ui.components.topappbar

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
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.util.fastFirst
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar(
    collapsedTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedContent: @Composable () -> Unit = {},
    collapsedHeight: Dp = CollapsedHeight,
    expandedHeight: Dp = ExpandedHeight,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    windowInsets: WindowInsets = CollapsingTopAppBarDefaults.windowInsets,
    colors: CollapsingTopAppBarColors = CollapsingTopAppBarDefaults.colors
) {
    require(collapsedHeight.isSpecified && collapsedHeight.isFinite) {
        "The collapsedHeight is expected to be specified and finite"
    }
    require(expandedHeight.isSpecified && expandedHeight.isFinite) {
        "The expandedHeight is expected to be specified and finite"
    }
    require(expandedHeight >= collapsedHeight) {
        "The expandedHeight is expected to be greater or equal to the collapsedHeight"
    }
    val colorTransitionFraction = { scrollBehavior?.state?.collapsedFraction ?: 0f }
    val appBarContainerColor = { colors.containerColor(colorTransitionFraction()) }
    val actionsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
    val titleAlpha = { TopTitleAlphaEasing.transform(colorTransitionFraction()) }
    val hideTitleSemantics by remember(colorTransitionFraction) { derivedStateOf { colorTransitionFraction() < 0.5f } }
    val appBarDragModifier =
        if (scrollBehavior != null && !scrollBehavior.isPinned) {
            Modifier.draggable(
                orientation = Orientation.Vertical,
                state =
                    rememberDraggableState { delta ->
                        scrollBehavior.state.heightOffset += delta
                    },
                onDragStopped = { velocity ->
                    settleAppBar(
                        scrollBehavior.state,
                        velocity,
                        scrollBehavior.flingAnimationSpec,
                        scrollBehavior.snapAnimationSpec,
                    )
                },
            )
        } else {
            Modifier
        }
    Box(
        modifier = modifier
            .then(appBarDragModifier)
            .drawBehind { drawRect(color = appBarContainerColor()) }
            .semantics { isTraversalGroup = true }
            .pointerInput(Unit) {}
    ) {
        val density = LocalDensity.current
        val collapsedHeightPx = remember(density, collapsedHeight, windowInsets) {
            with(density) {
                collapsedHeight.toPx() + windowInsets.getTop(this).toFloat()
            }
        }
        ExpandedContentLayout(
            modifier = Modifier
                .clipToBounds()
                .graphicsLayer { alpha = 1 - (scrollBehavior?.state?.collapsedFraction ?: 0f) }
                .adjustHeightOffsetLimit(scrollBehavior, collapsedHeightPx),
            scrolledOffset = { scrollBehavior?.state?.heightOffset ?: 0f },
            height = expandedHeight,
            content = expandedContent
        )
        CollapsedContentLayout(
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
                .clipToBounds(),
            scrolledOffset = { 0f },
            title = collapsedTitle,
            navigationIcon = navigationIcon,
            actions = actionsRow,
            titleContentColor = colors.titleContentColor,
            navigationIconContentColor = colors.navigationIconContentColor,
            actionIconContentColor = colors.actionsContentColor,
            titleAlpha = titleAlpha,
            titleTextStyle = titleTextStyle,
            hideTitleSemantics = hideTitleSemantics,
            height = collapsedHeight
        )
    }
}

@Composable
private fun ExpandedContentLayout(
    modifier: Modifier,
    scrolledOffset: FloatProducer,
    height: Dp,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val scrolledOffsetValue = scrolledOffset()
        val heightOffset = if (scrolledOffsetValue.isNaN()) 0 else scrolledOffsetValue.roundToInt()
        val heightPx = height.roundToPx()

        val layoutHeight =
            if (constraints.maxHeight == Constraints.Infinity) {
                constraints.maxHeight
            } else {
                (heightPx + heightOffset).coerceAtLeast(0)
            }


        val contentPlaceable = measurables.first().measure(
            constraints.copy(
                minWidth = constraints.maxWidth,
                minHeight = 0,
                maxHeight = heightPx
            )
        )

        layout(constraints.maxWidth, layoutHeight) {
            val x = (constraints.maxWidth - contentPlaceable.width) / 2
            val y = heightPx - contentPlaceable.height
            contentPlaceable.placeRelative(x = x, y = y)
        }
    }
}

@Composable
private fun CollapsedContentLayout(
    modifier: Modifier,
    scrolledOffset: FloatProducer,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    titleContentColor: Color,
    navigationIconContentColor: Color,
    actionIconContentColor: Color,
    titleAlpha: () -> Float,
    titleTextStyle: TextStyle,
    hideTitleSemantics: Boolean,
    height: Dp,
) {
    Layout(
        content = {
            Box(
                modifier = Modifier
                    .layoutId(NavigationIconId)
                    .padding(HorizontalPadding)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides navigationIconContentColor,
                    content = navigationIcon
                )
            }
            Box(
                modifier = Modifier
                    .layoutId(TitleId)
                    .padding(HorizontalPadding)
                    .then(if (hideTitleSemantics) Modifier.clearAndSetSemantics {} else Modifier)
                    .graphicsLayer { alpha = titleAlpha() }) {
                val mergedStyle = LocalTextStyle.current.merge(titleTextStyle)
                CompositionLocalProvider(
                    LocalTextStyle provides mergedStyle,
                    LocalContentColor provides titleContentColor,
                    content = title
                )
            }
            Box(
                modifier = Modifier
                    .layoutId(ActionsId)
                    .padding(HorizontalPadding)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides actionIconContentColor,
                    content = actions
                )
            }
        },
        modifier = modifier,
        measurePolicy = remember(scrolledOffset, height) {
            CollapsedTopAppBarMeasurePolicy(
                scrolledOffset,
                height
            )
        }
    )
}

private class CollapsedTopAppBarMeasurePolicy(val scrolledOffset: FloatProducer, val height: Dp) :
    MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val navigationIconPlaceable = measurables.fastFirst { it.layoutId == NavigationIconId }
            .measure(constraints.copy(minWidth = 0))
        val actionIconsPlaceable = measurables.fastFirst { it.layoutId == ActionsId }
            .measure(constraints.copy(minWidth = 0))
        val maxTitleWidth = if (constraints.maxWidth == Constraints.Infinity) {
            constraints.maxWidth
        } else {
            (constraints.maxWidth - navigationIconPlaceable.width - actionIconsPlaceable.width)
                .coerceAtLeast(0)
        }
        val titlePlaceable = measurables.fastFirst { it.layoutId == TitleId }
            .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))
        val scrolledOffsetValue = scrolledOffset()
        val heightOffset = if (scrolledOffsetValue.isNaN()) 0 else scrolledOffsetValue.roundToInt()
        val maxLayoutHeight = max(height.roundToPx(), titlePlaceable.height)
        val layoutHeight = if (constraints.maxHeight == Constraints.Infinity) {
            constraints.maxHeight
        } else {
            (maxLayoutHeight + heightOffset).coerceAtLeast(0)
        }

        return placeTopAppBar(
            constraints = constraints,
            layoutHeight = layoutHeight,
            navigationIconPlaceable = navigationIconPlaceable,
            titlePlaceable = titlePlaceable,
            actionIconsPlaceable = actionIconsPlaceable
        )
    }

    private fun MeasureScope.placeTopAppBar(
        constraints: Constraints,
        layoutHeight: Int,
        navigationIconPlaceable: Placeable,
        titlePlaceable: Placeable,
        actionIconsPlaceable: Placeable,
    ): MeasureResult = layout(constraints.maxWidth, layoutHeight) {
        navigationIconPlaceable.placeRelative(
            x = 0,
            y = (layoutHeight - navigationIconPlaceable.height) / 2
        )
        val titleX = max(TopAppBarTitleInset.roundToPx(), navigationIconPlaceable.width)
        val titleY = (layoutHeight - titlePlaceable.height) / 2
        titlePlaceable.placeRelative(x = titleX, y = titleY)
        actionIconsPlaceable.placeRelative(
            x = constraints.maxWidth - actionIconsPlaceable.width,
            y = (layoutHeight - actionIconsPlaceable.height) / 2
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?,
): Velocity {
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(initialValue = 0f, initialVelocity = velocity).animateDecay(
            flingAnimationSpec
        ) {
            val delta = value - lastValue
            val initialHeightOffset = state.heightOffset
            state.heightOffset = initialHeightOffset + delta
            val consumed = abs(initialHeightOffset - state.heightOffset)
            lastValue = value
            remainingVelocity = this.velocity
            if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
        }
    }
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 && state.heightOffset > state.heightOffsetLimit) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec,
            ) {
                state.heightOffset = value
            }
        }
    }

    return Velocity(0f, remainingVelocity)
}

@OptIn(ExperimentalMaterial3Api::class)
private fun Modifier.adjustHeightOffsetLimit(
    scrollBehavior: TopAppBarScrollBehavior?,
    collapsedHeight: Float
): Modifier = scrollBehavior?.state?.let {
    onSizeChanged { size ->
        val offset = size.height.toFloat() - collapsedHeight - it.heightOffset
        it.heightOffsetLimit = -offset
    }
} ?: this

private val CollapsedHeight = 64.dp
private val ExpandedHeight = 240.dp
private val HorizontalPadding = 4.dp
private val TopAppBarTitleInset = 16.dp - HorizontalPadding

private const val TitleId = "title"
private const val NavigationIconId = "navigationIcon"
private const val ActionsId = "actions"

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
    val colors
        @Composable get() = CollapsingTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
            titleContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
            actionsContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface)
        )
    val windowInsets
        @Composable get() =
            WindowInsets.statusBars.union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
}

private val TopTitleAlphaEasing = CubicBezierEasing(.8f, 0f, .8f, .15f)

private fun interface FloatProducer {
    operator fun invoke(): Float
}