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
import ru.d3rvich.core.model.ThemeType
import ru.d3rvich.feature.settings.ui.R

@Composable
internal fun ThemeMode(
    theme: ThemeType,
    onThemeChange: (ThemeType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.theme),
            fontSize = 18.sp,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .selectableGroup(),
        ) {
            SettingOptionItem(
                text = stringResource(id = R.string.theme_system),
                selected = theme == ThemeType.System,
                onClick = {
                    onThemeChange(ThemeType.System)
                })
            SettingOptionItem(
                text = stringResource(id = R.string.theme_light),
                selected = theme == ThemeType.Light,
                onClick = {
                    onThemeChange(ThemeType.Light)
                })
            SettingOptionItem(
                text = stringResource(id = R.string.theme_dark),
                selected = theme == ThemeType.Dark,
                onClick = {
                    onThemeChange(ThemeType.Dark)
                })
        }
    }
}