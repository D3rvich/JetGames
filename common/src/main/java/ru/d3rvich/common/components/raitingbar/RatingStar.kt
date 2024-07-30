package ru.d3rvich.common.components.raitingbar

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RatingStar(
    modifier: Modifier = Modifier,
    value: Float = 1f,
    size: Dp = RatingBarDefaults.RatingStarSize,
    strokeWidth: Dp = RatingBarDefaults.StrokeWidth,
    colors: RatingBarColors = RatingBarDefaults.RatingBarColors,
) {
    Box(
        modifier = modifier.size(size)
    ) {
        FilledStar(value = value, strokeWidth = strokeWidth, colors = colors)
        EmptyStar(value = value, strokeWidth = strokeWidth, colors = colors)
    }
}

@Composable
private fun FilledStar(
    modifier: Modifier = Modifier,
    value: Float,
    strokeWidth: Dp,
    colors: RatingBarColors,
) {
    val density = LocalDensity.current
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clip(FractionalStarShape(0f, value))
    ) {
        val path = Path().addStar(size)

        drawPath(path = path, color = colors.activeColor)
        val strokeWidthPx = with(density) { strokeWidth.toPx() }
        drawPath(path = path, color = colors.activeStrokeColor, style = Stroke(strokeWidthPx))
    }
}

@Composable
private fun EmptyStar(
    modifier: Modifier = Modifier,
    value: Float,
    strokeWidth: Dp,
    colors: RatingBarColors,
) {
    val density = LocalDensity.current
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clip(FractionalStarShape(value, 1f))
    ) {
        val path = Path().addStar(size)
        drawPath(path = path, color = colors.inActiveColor)
        val strokeWidthPx = with(density) { strokeWidth.toPx() }
        drawPath(path = path, color = colors.isActiveStrokeColor, style = Stroke(strokeWidthPx))
    }
}

private fun Path.addStar(
    size: Size,
    spikes: Int = 5,
    @FloatRange(from = 0.0, to = 0.5) outerRadiusFraction: Float = 0.5f,
    @FloatRange(from = 0.0, to = 0.5) innerRadiusFraction: Float = 0.2f,
): Path {
    val outerRadius = size.minDimension * outerRadiusFraction
    val innerRadius = size.minDimension * innerRadiusFraction

    val centerX = size.width / 2
    val centerY = size.height / 2

    var totalAngle = PI / 2 // Since we start at the top center, the initial angle will be 90°
    val degreesPerSection = (2 * PI) / spikes

    moveTo(centerX, 0f) // Starts at the top center of the bounds

    var x: Double
    var y: Double

    repeat(spikes) {
        // Line going inwards from outerCircle to innerCircle
        totalAngle += degreesPerSection / 2
        x = centerX + cos(totalAngle) * innerRadius
        y = centerY - sin(totalAngle) * innerRadius
        lineTo(x.toFloat(), y.toFloat())


        // Line going outwards from innerCircle to outerCircle
        totalAngle += degreesPerSection / 2
        x = centerX + cos(totalAngle) * outerRadius
        y = centerY - sin(totalAngle) * outerRadius
        lineTo(x.toFloat(), y.toFloat())
    }

    // Path should be closed to ensure it's not an open shape
    close()

    return this
}

@Preview(showBackground = true)
@Composable
private fun FilledStarPreview() {
    MaterialTheme {
        Box(modifier = Modifier.size(100.dp)) {
            FilledStar(
                value = 1.0f,
                strokeWidth = 1.dp,
                colors = RatingBarDefaults.RatingBarColors
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStarPreview() {
    MaterialTheme {
        Box(modifier = Modifier.size(100.dp)) {
            EmptyStar(
                value = 0.0f,
                strokeWidth = 1.dp,
                colors = RatingBarDefaults.RatingBarColors
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingStarPreview() {
    MaterialTheme {
        Row {
            RatingStar(
                value = 1.0f,
                size = 100.dp,
                strokeWidth = 1.dp,
                colors = RatingBarDefaults.RatingBarColors
            )
            RatingStar(
                value = 0.5f,
                size = 100.dp,
                strokeWidth = 1.dp,
                colors = RatingBarDefaults.RatingBarColors
            )
            RatingStar(
                value = 0.0f,
                size = 100.dp,
                strokeWidth = 1.dp,
                colors = RatingBarDefaults.RatingBarColors
            )
        }
    }
}