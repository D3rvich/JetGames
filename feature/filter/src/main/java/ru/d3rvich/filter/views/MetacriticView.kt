package ru.d3rvich.filter.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.filter.R
import kotlin.math.roundToInt

@Composable
internal fun MetacriticView(
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    BaseFilterView(
        modifier = modifier,
        label = stringResource(id = R.string.metacritic_label),
        trailingIcon = { isOpen ->
            ChangeVisibilityContainerDefaults.DefaultIcon(isOpen = isOpen)
        }) {
        MetacriticViewContent(range = range, onRangeChange = onRangeChange)
    }
}

@Composable
private fun MetacriticViewContent(
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 12.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RangeSlider(
            valueRange = 0f..100f,
            value = range,
            onValueChange = onRangeChange
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = range.start.roundToInt().toString())
            Text(text = range.endInclusive.roundToInt().toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MetacriticViewPreview() {
    JetGamesTheme {
        MetacriticView(0f..100f, onRangeChange = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun MetacriticInnerViewPreview() {
    JetGamesTheme {
        MetacriticViewContent(0f..100f, onRangeChange = { })
    }
}