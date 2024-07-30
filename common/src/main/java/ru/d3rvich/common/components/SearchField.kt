package ru.d3rvich.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.d3rvich.common.R

/**
 * Created by Ilya Deryabin at 27.02.2024
 */
@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    focusRequester: FocusRequester? = null,
    onClearText: (() -> Unit)? = null,
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text(text = stringResource(R.string.search)) },
        prefix = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                modifier = Modifier.padding(end = 4.dp)
            )
        },
        suffix = {
            IconButton(onClick = {
                onClearText?.invoke()
                if (text.isNotEmpty()) {
                    onTextChange("")
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.clear)
                )
            }
        },
        singleLine = true,
        modifier = modifier.then(
            if (focusRequester != null) {
                Modifier.focusRequester(focusRequester)
            } else {
                Modifier
            }
        ),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}