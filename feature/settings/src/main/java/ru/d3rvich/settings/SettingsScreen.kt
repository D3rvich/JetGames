package ru.d3rvich.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.core.ui.utils.ColorModeType
import ru.d3rvich.core.ui.utils.SettingsEventBus
import ru.d3rvich.core.ui.utils.ThemeType

/**
 * Created by Ilya Deryabin at 05.09.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navigateBack: () -> Unit = {}) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            ThemeMode(modifier = Modifier.padding(top = 8.dp))
            HorizontalDivider()
            DynamicTheme(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun ThemeMode(modifier: Modifier = Modifier) {
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
            val currentTheme by SettingsEventBus.currentTheme.collectAsStateWithLifecycle()
            SettingOptionItem(
                text = stringResource(id = ThemeType.System.titleResId),
                selected = currentTheme == ThemeType.System,
                onClick = {
                    SettingsEventBus.setCurrentTheme(ThemeType.System)
                })
            SettingOptionItem(
                text = stringResource(id = ThemeType.Light.titleResId),
                selected = currentTheme == ThemeType.Light,
                onClick = {
                    SettingsEventBus.setCurrentTheme(ThemeType.Light)
                })
            SettingOptionItem(
                text = stringResource(id = ThemeType.Dark.titleResId),
                selected = currentTheme == ThemeType.Dark,
                onClick = {
                    SettingsEventBus.setCurrentTheme(ThemeType.Dark)
                })
        }
    }
}

@Composable
private fun DynamicTheme(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.color_mode),
            fontSize = 18.sp,
        )
        val dynamicColor by SettingsEventBus.colorMode.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .selectableGroup()
        ) {
            SettingOptionItem(
                text = stringResource(ColorModeType.Default.titleResId),
                selected = dynamicColor == ColorModeType.Default,
                onClick = {
                    SettingsEventBus.setColorMode(ColorModeType.Default)
                }
            )
            SettingOptionItem(
                text = stringResource(ColorModeType.Dynamic.titleResId),
                selected = dynamicColor == ColorModeType.Dynamic,
                onClick = {
                    SettingsEventBus.setColorMode(ColorModeType.Dynamic)
                },
                enabled = SettingsEventBus.isDynamicColorSupported
            )
        }
    }
}

@Composable
private fun SettingOptionItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .selectable(
                selected = false,
                role = Role.RadioButton,
                enabled = enabled,
                onClick = onClick
            )
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    JetGamesTheme {
        SettingsScreen()
    }
}