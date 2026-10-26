package pe.edu.upc.calculator.features.calculator.presentation.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextLayoutResult
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase

fun calculatorRawCursorPosition(
    expression: String,
    visualPosition: Int,
    displayLayout: CalculatorDisplayLayout,
    formatExpressionUseCase: FormatExpressionUseCase
): Int {
    val safePosition = visualPosition.coerceIn(
        0,
        displayLayout.visualDisplay.length
    )

    return formatExpressionUseCase.rawPosition(
        expression = expression,
        formattedPosition = displayLayout.toFormattedPosition(safePosition)
    )
}

fun calculatorVisualCursorPosition(
    expression: String,
    rawPosition: Int,
    formattedDisplay: String,
    displayLayout: CalculatorDisplayLayout,
    formatExpressionUseCase: FormatExpressionUseCase
): Int {
    val formattedCursorPosition = formatExpressionUseCase.formattedPosition(
        expression = expression,
        rawPosition = rawPosition
    ).coerceIn(0, formattedDisplay.length)

    return displayLayout
        .toVisualPosition(formattedCursorPosition)
        .coerceIn(0, displayLayout.visualDisplay.length)
}

fun calculatorCursorPositionFromOffset(
    offset: Offset,
    textLayoutResult: TextLayoutResult,
    expression: String,
    displayLayout: CalculatorDisplayLayout,
    formatExpressionUseCase: FormatExpressionUseCase
): Int {
    val visualPosition = textLayoutResult
        .getOffsetForPosition(offset)
        .coerceIn(0, displayLayout.visualDisplay.length)

    return calculatorRawCursorPosition(
        expression = expression,
        visualPosition = visualPosition,
        displayLayout = displayLayout,
        formatExpressionUseCase = formatExpressionUseCase
    )
}

fun calculatorCursorVerticalBounds(
    display: String,
    layout: TextLayoutResult,
    cursorPosition: Int,
    canvasHeight: Float,
    emptyCursorHeight: Float
): Pair<Float, Float> {
    if (display.isEmpty()) {
        val top = (canvasHeight - emptyCursorHeight) / 2f
        return top to (top + emptyCursorHeight)
    }

    val cursorRect = layout.getCursorRect(cursorPosition)
    return cursorRect.top to cursorRect.bottom
}