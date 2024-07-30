package ru.d3rvich.filter.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.d3rvich.filter.R

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterAppBar(
    modifier: Modifier = Modifier,
    isResetButtonVisible: Boolean,
    onBackClicked: () -> Unit,
    onResetClicked: () -> Unit,
) {
    TopAppBar(modifier = modifier,
        title = { Text(text = stringResource(R.string.filter_label)) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        },
        actions = {
            AnimatedVisibility(
                visible = isResetButtonVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TextButton(onClick = onResetClicked) {
                    Text(text = stringResource(R.string.reset))
                }
            }
        }
    )
}