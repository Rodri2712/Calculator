package pe.edu.upc.calculator.features.calculator.presentation.utils

import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase

fun hasCalculatorOperation(display: String): Boolean {
    return display.any { it == '+' || it == '×' || it == '÷' } ||
            display.drop(1).any { it == '-' || it == '−' }
}

fun shouldShowCalculatorPreview(
    showingResult: Boolean,
    hasError: Boolean,
    display: String,
    hasOperation: Boolean
): Boolean {
    return !showingResult &&
            !hasError &&
            display != "Error" &&
            hasOperation
}

fun formatCalculatorPreview(
    preview: String?,
    formatExpressionUseCase: FormatExpressionUseCase
): String {
    return preview?.let { expression ->
        formatExpressionUseCase(expression)
    }.orEmpty()
}