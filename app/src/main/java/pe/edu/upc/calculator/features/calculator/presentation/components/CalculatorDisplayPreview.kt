package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.runtime.Composable
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase
import pe.edu.upc.calculator.features.calculator.presentation.utils.hasCalculatorOperation
import pe.edu.upc.calculator.features.calculator.presentation.utils.shouldShowCalculatorPreview

@Composable
fun CalculatorDisplayPreview(
    display: String,
    showingResult: Boolean,
    hasError: Boolean,
    previewResult: (String) -> String?,
    formatExpressionUseCase: FormatExpressionUseCase
) {
    val hasOperation = hasCalculatorOperation(display)

    val preview = if (
        shouldShowCalculatorPreview(
            showingResult = showingResult,
            hasError = hasError,
            display = display,
            hasOperation = hasOperation
        )
    ) {
        previewResult(display)
    } else {
        null
    }

    CalculatorPreviewDisplay(
        preview = preview,
        formatExpressionUseCase = formatExpressionUseCase
    )
}