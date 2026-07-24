package ru.d3rvich.feature.settings.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.feature.settings.ui.R

@Composable
internal fun DynamicTheme(
    isDynamicColorSupported: Boolean,
    colorMode: ColorMode,
    onColorModeChange: (ColorMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.color_mode),
            fontSize = 18.sp,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .selectableGroup()
        ) {
            SettingOptionItem(
                text = stringResource(R.string.color_mode_default),
                selected = colorMode == ColorMode.Default,
                onClick = {
                    onColorModeChange(ColorMode.Default)
                }
            )
            SettingOptionItem(
                text = stringResource(R.string.color_mode_dynamic),
                selected = colorMode == ColorMode.Dynamic,
                onClick = {
                    onColorModeChange(ColorMode.Dynamic)
                },
                enabled = isDynamicColorSupported
            )
        }
    }
}