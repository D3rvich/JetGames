package ru.d3rvich.feature.settings.impl

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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.common.R as uiR

/**
 * Created by Ilya Deryabin at 05.09.2024
 */
@Composable
internal fun SettingsContent(
    component: SettingsComponent,
    modifier: Modifier = Modifier
) {
    val model by component.models.subscribeAsState()
    SettingsContent(
        modifier = modifier,
        model = model,
        onThemeChange = component::setThemeType,
        onColorModeChange = component::setColorMode,
        navigateBack = component::onCloseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    model: SettingsComponent.Model,
    modifier: Modifier = Modifier,
    onThemeChange: (ThemeType) -> Unit = {},
    onColorModeChange: (ColorMode) -> Unit = {},
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
        when (model) {
            SettingsComponent.Model.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SettingsComponent.Model.Settings -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    ThemeMode(
                        modifier = Modifier.padding(top = 8.dp),
                        theme = model.themeType,
                        onThemeChange = onThemeChange
                    )
                    HorizontalDivider()
                    DynamicTheme(
                        modifier = Modifier.padding(top = 8.dp),
                        colorMode = model.colorMode,
                        isDynamicColorSupported = model.inDynamicColorSupported,
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
        SettingsContent(model = SettingsComponent.Model.Loading)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    JetGamesTheme {
        SettingsContent(
            model = SettingsComponent.Model.Settings(
                themeType = ThemeType.System,
                colorMode = ColorMode.Default,
                inDynamicColorSupported = false
            )
        )
    }
}