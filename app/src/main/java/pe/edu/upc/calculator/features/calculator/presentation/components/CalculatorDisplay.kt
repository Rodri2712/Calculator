package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import pe.edu.upc.calculator.features.calculator.presentation.CalculatorUiState

@Composable
fun CalculatorDisplay(
    state: CalculatorUiState,
    previewResult: (String) -> String?,
    onCursorPositionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val display = state.display

    val displayText = buildAnnotatedString {
        append(display)

        display.forEachIndexed { index, character ->
            if (character in listOf('+', '-', '−', '×', '÷', '%')) {
                addStyle(
                    style = SpanStyle(color = Color(0xFF2583D8)),
                    start = index,
                    end = index + 1
                )
            }
        }

        val start = state.errorStartIndex
        val end = state.errorEndIndex

        if (
            state.error != null &&
            start != null &&
            end != null &&
            start >= 0 &&
            end > start &&
            end <= display.length
        ) {
            addStyle(
                style = SpanStyle(color = Color(0xFFFF5252)),
                start = start,
                end = end
            )
        }
    }

    val showingResult = state.isResultDisplayed

    val hasOperation =
        display.any { it == '+' || it == '×' || it == '÷' } ||
                display.drop(1).any { it == '-' || it == '−' }

    val preview = if (
        !showingResult &&
        state.error == null &&
        display != "Error" &&
        hasOperation
    ) {
        previewResult(display)
    } else {
        null
    }

    val targetFontSize = when {
        showingResult && display.length > 30 -> 18.sp
        showingResult && display.length > 24 -> 22.sp
        showingResult && display.length > 19 -> 28.sp
        showingResult && display.length > 14 -> 38.sp
        showingResult && display.length > 10 -> 52.sp
        showingResult -> 84.sp

        display.length > 30 -> 16.sp
        display.length > 24 -> 20.sp
        display.length > 19 -> 26.sp
        display.length > 14 -> 34.sp
        display.length > 10 -> 46.sp
        else -> 76.sp
    }

    val animatedFontSize by animateFloatAsState(
        targetValue = targetFontSize.value,
        animationSpec = tween(
            durationMillis = 180,
            easing = LinearOutSlowInEasing
        ),
        label = "display_font_size"
    )

    var textLayoutResult by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }

    val cursorPosition = state.cursorPosition.coerceIn(0, display.length)

    val cursorTransition = rememberInfiniteTransition(
        label = "calculator_cursor"
    )

    val cursorAlpha by cursorTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = displayText,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(display, textLayoutResult) {
                    detectTapGestures { tapOffset ->
                        textLayoutResult?.let { layout ->
                            val position = layout
                                .getOffsetForPosition(tapOffset)
                                .coerceIn(0, display.length)

                            onCursorPositionChange(position)
                        }
                    }
                },
            color = Color.White,
            fontSize = animatedFontSize.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
            onTextLayout = { layout ->
                textLayoutResult = layout
            }
        )

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(display, showingResult) {
                    detectTapGestures { offset ->
                        val layout = textLayoutResult

                        if (layout != null && !showingResult) {
                            val position = layout
                                .getOffsetForPosition(offset)
                                .coerceIn(0, display.length)

                            onCursorPositionChange(position)
                        }
                    }
                }
                .pointerInput(display, showingResult) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val layout = textLayoutResult

                            if (layout != null && !showingResult) {
                                val position = layout
                                    .getOffsetForPosition(offset)
                                    .coerceIn(0, display.length)

                                onCursorPositionChange(position)
                            }
                        },
                        onDrag = { change, _ ->
                            val layout = textLayoutResult

                            if (layout != null && !showingResult) {
                                val position = layout
                                    .getOffsetForPosition(change.position)
                                    .coerceIn(0, display.length)

                                onCursorPositionChange(position)
                                change.consume()
                            }
                        }
                    )
                }
        ) {
            val layout = textLayoutResult

            if (layout != null && !showingResult && display.isNotEmpty()) {
                val cursorRect = layout.getCursorRect(cursorPosition)

                drawLine(
                    color = Color(0xFF55B8FF).copy(alpha = cursorAlpha),
                    start = Offset(cursorRect.left, cursorRect.top),
                    end = Offset(cursorRect.left, cursorRect.bottom),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = preview != null,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 70)
            )
        ) {
            Text(
                text = preview.orEmpty(),
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF666666),
                fontSize = 30.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
        }
    }
}