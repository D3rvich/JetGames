package ru.d3rvich.common.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import ru.d3rvich.common.R

/**
 * Created by Ilya Deryabin at 14.03.2024
 */
@Composable
fun CollapsingText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    collapsedMaxLines: Int = DefaultMinimumTextLines,
    showMoreText: String = stringResource(R.string.show_more),
    showMoreTextStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String = stringResource(R.string.show_less),
    showLessTextStyle: SpanStyle = showMoreTextStyle,
) {
    var clickable: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var isExpanded: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var lastCharIndex: Int by rememberSaveable {
        mutableIntStateOf(text.length - 1)
    }
    Text(
        modifier = modifier
            .clickable(clickable) { isExpanded = !isExpanded }
            .fillMaxWidth()
            .animateContentSize(),
        text = buildAnnotatedString {
            if (clickable) {
                if (isExpanded) {
                    append(text)
                    withStyle(style = showLessTextStyle) {
                        append(" $showLessText")
                    }
                } else {
                    val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                        .dropLast(showMoreText.length)
                        .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                    append(adjustText)
                    withStyle(style = showMoreTextStyle) {
                        append(showMoreText)
                    }
                }
            } else {
                append(text)
            }
        },
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                clickable = true
                lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLines - 1)
            }
        },
        style = style,
        fontSize = fontSize,
        fontStyle = fontStyle,
        textAlign = textAlign
    )
}

private const val DefaultMinimumTextLines = 3