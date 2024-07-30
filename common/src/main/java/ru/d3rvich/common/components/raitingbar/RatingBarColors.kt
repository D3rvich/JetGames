package ru.d3rvich.common.components.raitingbar

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class RatingBarColors(
    val activeColor: Color,
    val inActiveColor: Color,
    val activeStrokeColor: Color = activeColor,
    val isActiveStrokeColor: Color = activeStrokeColor,
)