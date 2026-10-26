package pe.edu.upc.calculator.features.calculator.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import pe.edu.upc.calculator.core.designsystems.theme.ErrorTextColor
import pe.edu.upc.calculator.core.designsystems.theme.ExpressionOperatorColor
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase

data class CalculatorErrorRange(
    val start: Int,
    val end: Int
)

fun calculatorErrorRange(
    expression: String,
    rawStart: Int,
    rawEnd: Int,
    displayLayout: CalculatorDisplayLayout,
    formatExpressionUseCase: FormatExpressionUseCase
): CalculatorErrorRange? {
    if (rawStart !in 0 until rawEnd || rawEnd > expression.length) {
        return null
    }

    val formattedStart = formatExpressionUseCase.formattedPosition(
        expression = expression,
        rawPosition = rawStart
    )

    val formattedEnd = formatExpressionUseCase.formattedPosition(
        expression = expression,
        rawPosition = rawEnd
    )

    val visualStart = displayLayout.toVisualPosition(formattedStart)
    val visualEnd = displayLayout.toVisualPosition(formattedEnd)

    return if (visualEnd > visualStart) {
        CalculatorErrorRange(
            start = visualStart,
            end = visualEnd
        )
    } else {
        null
    }
}

fun buildCalculatorDisplayText(
    visualDisplay: String,
    errorRange: CalculatorErrorRange?
): AnnotatedString {
    return buildAnnotatedString {
        visualDisplay.forEachIndexed { index, character ->
            append(character)

            if (character in listOf('+', '-', '−', '×', '÷', '%')) {
                addStyle(
                    style = SpanStyle(color = ExpressionOperatorColor),
                    start = index,
                    end = index + 1
                )
            }
        }

        errorRange?.let { range ->
            addStyle(
                style = SpanStyle(color = ErrorTextColor),
                start = range.start,
                end = range.end
            )
        }
    }
}