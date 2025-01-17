package ru.d3rvich.browse.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.d3rvich.browse.R
import ru.d3rvich.core.ui.theme.JetGamesTheme

/**
 * Created by Ilya Deryabin at 29.10.2024
 */
@Composable
internal fun BrowseSectionItemView(
    modifier: Modifier = Modifier,
    itemName: String,
    itemCount: Int,
    itemImageUrl: String?,
) {
    Card(
        modifier = modifier.size(180.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = itemImageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = itemName, color = Color(0xFFECECEC))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = stringResource(R.string.games))
                    Text(text = itemCount.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrowseSectionItemViewPreview() {
    JetGamesTheme {
        BrowseSectionItemView(
            itemName = "Item name",
            itemCount = 42,
            itemImageUrl = null
        )
    }
}