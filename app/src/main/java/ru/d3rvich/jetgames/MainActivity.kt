package ru.d3rvich.jetgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.ui.model.asUiState
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.root.component.RootComponent
import ru.d3rvich.feature.root.ui.RootContent

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    private val rootComponentFactory: RootComponent.Factory by inject()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.shouldKeepSplash()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val root = rootComponentFactory.create(defaultComponentContext())
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
            val userPreferences =
                (uiState as? MainActivityUiState.Success)?.userPreferences ?: UserPreferences()
            JetGamesTheme(userPreferencesUiState = userPreferences.asUiState()) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootContent(root, windowSizeClass)
                }
            }
        }
    }
}