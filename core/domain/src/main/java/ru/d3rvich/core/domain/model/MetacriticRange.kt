package ru.d3rvich.core.domain.model

import androidx.annotation.FloatRange

/**
 * Created by Ilya Deryabin at 16.05.2024
 */
data class MetacriticRange(
    @FloatRange(from = 0.0, to = 100.0) val min: Float = 0f,
    @FloatRange(from = 0.0, to = 100.0) val max: Float = 100f,
) {
    companion object {
        val None = MetacriticRange(min = Float.NaN, max = Float.NaN)
    }
}

fun MetacriticRange(floatRange: ClosedFloatingPointRange<Float>): MetacriticRange =
    MetacriticRange(min = floatRange.start, max = floatRange.endInclusive)