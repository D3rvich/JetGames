package ru.d3rvich.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.core.ui.theme.JetGamesTheme
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
            Text(
                text = "Theme mode",
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Column(modifier = Modifier.selectableGroup()) {
                val currentTheme by SettingsEventBus.currentTheme.collectAsStateWithLifecycle()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(false) {
                            SettingsEventBus.setCurrentTheme(ThemeType.SystemDefault)
                        }
                ) {
                    RadioButton(
                        selected = currentTheme == ThemeType.SystemDefault, onClick = null,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(text = stringResource(id = ThemeType.SystemDefault.titleResId))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(false) {
                            SettingsEventBus.setCurrentTheme(ThemeType.Light)
                        }
                ) {
                    RadioButton(
                        selected = currentTheme == ThemeType.Light, onClick = null,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(text = stringResource(id = ThemeType.Light.titleResId))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(false) {
                            SettingsEventBus.setCurrentTheme(ThemeType.Dark)
                        }
                ) {
                    RadioButton(
                        selected = currentTheme == ThemeType.Dark, onClick = null,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(text = stringResource(id = ThemeType.Dark.titleResId))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    JetGamesTheme {
        SettingsScreen()
    }
}