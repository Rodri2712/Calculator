package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.calculator.core.designsystems.theme.CursorColor
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase
import pe.edu.upc.calculator.features.calculator.presentation.animations.rememberCalculatorCursorAlpha
import pe.edu.upc.calculator.features.calculator.presentation.utils.CalculatorDisplayLayout
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorCursorPositionFromOffset
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorCursorVerticalBounds
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorVisualCursorPosition

@Composable
fun CalculatorExpressionDisplay(
    display: String,
    formattedDisplay: String,
    displayText: androidx.compose.ui.text.AnnotatedString,
    displayLayout: CalculatorDisplayLayout,
    showingResult: Boolean,
    cursorPosition: Int,
    formatExpressionUseCase: FormatExpressionUseCase,
    emptyCursorHeight: Float,
    fontSize: Float,
    modifier: Modifier = Modifier,
    onCursorPositionChange: (Int) -> Unit
) {
    var textLayoutResult by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }

    val visualCursorPosition = calculatorVisualCursorPosition(
        expression = display,
        rawPosition = cursorPosition,
        formattedDisplay = formattedDisplay,
        displayLayout = displayLayout,
        formatExpressionUseCase = formatExpressionUseCase
    )

    val cursorAlpha = rememberCalculatorCursorAlpha()

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
                            onCursorPositionChange(
                                calculatorCursorPositionFromOffset(
                                    offset = tapOffset,
                                    textLayoutResult = layout,
                                    expression = display,
                                    displayLayout = displayLayout,
                                    formatExpressionUseCase = formatExpressionUseCase
                                )
                            )
                        }
                    }
                },
            color = Color.White,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            maxLines = Int.MAX_VALUE,
            softWrap = true,
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
                            onCursorPositionChange(
                                calculatorCursorPositionFromOffset(
                                    offset = offset,
                                    textLayoutResult = layout,
                                    expression = display,
                                    displayLayout = displayLayout,
                                    formatExpressionUseCase = formatExpressionUseCase
                                )
                            )
                        }
                    }
                }
                .pointerInput(display, showingResult) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val layout = textLayoutResult

                            if (layout != null && !showingResult) {
                                onCursorPositionChange(
                                    calculatorCursorPositionFromOffset(
                                        offset = offset,
                                        textLayoutResult = layout,
                                        expression = display,
                                        displayLayout = displayLayout,
                                        formatExpressionUseCase = formatExpressionUseCase
                                    )
                                )
                            }
                        },
                        onDrag = { change, _ ->
                            val layout = textLayoutResult

                            if (layout != null && !showingResult) {
                                onCursorPositionChange(
                                    calculatorCursorPositionFromOffset(
                                        offset = change.position,
                                        textLayoutResult = layout,
                                        expression = display,
                                        displayLayout = displayLayout,
                                        formatExpressionUseCase = formatExpressionUseCase
                                    )
                                )
                                change.consume()
                            }
                        }
                    )
                }
        ) {
            val layout = textLayoutResult

            if (layout != null && !showingResult) {
                val cursorRect = layout.getCursorRect(visualCursorPosition)

                val (cursorTop, cursorBottom) = calculatorCursorVerticalBounds(
                    display = display,
                    layout = layout,
                    cursorPosition = visualCursorPosition,
                    canvasHeight = size.height,
                    emptyCursorHeight = emptyCursorHeight
                )

                drawLine(
                    color = CursorColor.copy(alpha = cursorAlpha),
                    start = Offset(cursorRect.left, cursorTop),
                    end = Offset(cursorRect.left, cursorBottom),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}