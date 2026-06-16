package ru.d3rvich.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.d3rvich.core.domain.model.ColorModeType
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.settings.store.SettingsStore
import ru.d3rvich.common.R as uiR

/**
 * Created by Ilya Deryabin at 05.09.2024
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    navigateBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        modifier = modifier,
        state = state,
        onThemeChange = { theme ->
            viewModel.obtainIntent(SettingsStore.Intent.ThemeTypeSelected(theme))
        },
        onColorModeChange = { colorCode ->
            viewModel.obtainIntent(SettingsStore.Intent.ColorModeSelected(colorCode))
        },
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsStore.State,
    modifier: Modifier = Modifier,
    onThemeChange: (ThemeType) -> Unit = {},
    onColorModeChange: (ColorModeType) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(uiR.drawable.arrow_back_24px),
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                })
        }) { paddingValues ->
        when (state) {
            SettingsStore.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SettingsStore.State.Settings -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    ThemeMode(
                        modifier = Modifier.padding(top = 8.dp),
                        theme = state.themeType,
                        onThemeChange = onThemeChange
                    )
                    HorizontalDivider()
                    DynamicTheme(
                        modifier = Modifier.padding(top = 8.dp),
                        colorMode = state.colorModeType,
                        isDynamicColorSupported = state.inDynamicColorSupported,
                        onColorModeChange = onColorModeChange
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeMode(
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

@Composable
private fun DynamicTheme(
    isDynamicColorSupported: Boolean,
    colorMode: ColorModeType,
    onColorModeChange: (ColorModeType) -> Unit,
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
                selected = colorMode == ColorModeType.Default,
                onClick = {
                    onColorModeChange(ColorModeType.Default)
                }
            )
            SettingOptionItem(
                text = stringResource(R.string.color_mode_dynamic),
                selected = colorMode == ColorModeType.Dynamic,
                onClick = {
                    onColorModeChange(ColorModeType.Dynamic)
                },
                enabled = isDynamicColorSupported
            )
        }
    }
}

@Composable
private fun SettingOptionItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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

@Preview
@Composable
private fun SettingsScreenPreview_Loading() {
    JetGamesTheme {
        SettingsScreen(state = SettingsStore.State.Loading)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    JetGamesTheme {
        SettingsScreen(
            state = SettingsStore.State.Settings(
                themeType = ThemeType.System,
                colorModeType = ColorModeType.Default,
                inDynamicColorSupported = false
            )
        )
    }
}