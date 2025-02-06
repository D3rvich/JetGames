package ru.d3rvich.jetgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.core.ui.utils.ColorModeType
import ru.d3rvich.core.ui.utils.SettingsEventBus
import ru.d3rvich.core.ui.utils.ThemeType
import ru.d3rvich.jetgames.navigation.SetupNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val currentTheme by SettingsEventBus.currentTheme.collectAsStateWithLifecycle()
            val dynamicColor =
                SettingsEventBus.colorMode.collectAsStateWithLifecycle().value == ColorModeType.Dynamic
            JetGamesTheme(
                darkTheme = when (currentTheme) {
                    ThemeType.Light -> false
                    ThemeType.Dark -> true
                    ThemeType.System -> isSystemInDarkTheme()
                },
                dynamicColor = dynamicColor
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(
                        windowSizeClass = windowSizeClass,
                    )
                }
            }
        }
    }
}
