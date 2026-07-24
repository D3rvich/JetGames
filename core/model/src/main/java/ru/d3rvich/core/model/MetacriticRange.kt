package ru.d3rvich.core.model

/**
 * Created by Ilya Deryabin at 16.05.2024
 */
data class MetacriticRange(val min: Float = 0f, val max: Float = 100f) {
    companion object {
        val Unspecific = MetacriticRange(min = 0f, max = 100f)
    }
}

fun MetacriticRange(floatRange: ClosedFloatingPointRange<Float>): MetacriticRange =
    MetacriticRange(min = floatRange.start, max = floatRange.endInclusive)