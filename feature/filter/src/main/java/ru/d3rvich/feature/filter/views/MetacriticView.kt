package ru.d3rvich.feature.filter.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.filter.R
import kotlin.math.roundToInt

@Composable
internal fun MetacriticView(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showInnerContent by rememberSaveable { mutableStateOf(false) }
    BaseFilterView(
        modifier = modifier,
        isInnerContainerVisible = showInnerContent,
        onInnerContainerVisibilityChange = { showInnerContent = it },
        label = stringResource(id = R.string.metacritic_label),
        trailingIcon = {
            ChangeVisibilityContainerDefaults.DefaultIcon(isOpen = showInnerContent)
        }) {
        MetacriticViewContent(range = range, onRangeChange = onRangeChange)
    }
}

@Composable
private fun MetacriticViewContent(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
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