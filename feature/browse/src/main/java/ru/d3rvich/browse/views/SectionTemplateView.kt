package ru.d3rvich.browse.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.domain.model.LoadingResult

/**
 * Created by Ilya Deryabin at 29.10.2024
 */
@Composable
internal fun <T> SectionTemplateView(
    modifier: Modifier = Modifier,
    name: String,
    status: LoadingResult<T>,
    content: @Composable (T) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.height(180.dp), contentAlignment = Alignment.Center) {
            when (status) {
                is LoadingResult.Error -> {
                    Text(text = "Error", modifier = Modifier.fillMaxSize())
                }

                LoadingResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LoadingResult.Success -> {
                    content(status.value)
                }
            }
        }
    }
}