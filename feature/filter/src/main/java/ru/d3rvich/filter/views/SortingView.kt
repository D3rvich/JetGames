package ru.d3rvich.filter.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.common.R
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.filter.R as FilterR

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SortingView(
    sortingList: List<SortingEntity>,
    selectedSorting: SortingEntity,
    isReversed: Boolean,
    modifier: Modifier = Modifier,
    onSortingSelected: (SortingEntity) -> Unit,
    onReversedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var expanded by remember {
            mutableStateOf(false)
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedSorting.getStringRes(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                sortingList.forEach { sorting ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = sorting.getStringRes(),
                                fontWeight = if (sorting == selectedSorting) FontWeight.Bold else null
                            )
                        },
                        onClick = {
                            onSortingSelected(sorting)
                            expanded = false
                        })
                }
            }
        }
        IconButton(onClick = { onReversedChange(!isReversed) }) {
            val iconRotation = animateFloatAsState(
                targetValue = if (isReversed) 0f else 180f,
                label = "iconRotation"
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_downward_24),
                contentDescription = null,
                modifier = Modifier.graphicsLayer(rotationZ = iconRotation.value)
            )
        }
    }
}

@Composable
private fun SortingEntity.getStringRes(): String =
    stringResource(
        id = when (this) {
            SortingEntity.NoSorting -> FilterR.string.no_sorting
            SortingEntity.Metacritic -> FilterR.string.metacritic
            SortingEntity.Rating -> FilterR.string.rating
            SortingEntity.Name -> FilterR.string.name
            SortingEntity.Released -> FilterR.string.released
        }
    )

@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun SortingViewPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        SortingView(
            sortingList = SortingEntity.entries,
            selectedSorting = SortingEntity.Rating,
            isReversed = true,
            onSortingSelected = {},
            onReversedChange = {}
        )
    }
}