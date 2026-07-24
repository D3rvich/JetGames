package ru.d3rvich.feature.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.d3rvich.common.R as uiR
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.feature.settings.ui.view.DynamicTheme
import ru.d3rvich.feature.settings.ui.view.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    val model by component.models.subscribeAsState()
    SettingsContent(
        model = model,
        onThemeChange = component::setThemeType,
        onColorModeChange = component::setColorMode,
        navigateBack = component::onClose,
        modifier = modifier
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