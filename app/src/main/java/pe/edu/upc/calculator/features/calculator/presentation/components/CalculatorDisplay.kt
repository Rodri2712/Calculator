package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorDisplayFontSize
import pe.edu.upc.calculator.features.calculator.presentation.utils.createCalculatorDisplayLayout
import pe.edu.upc.calculator.features.calculator.presentation.animations.rememberCalculatorDisplayFontSize
import pe.edu.upc.calculator.features.calculator.presentation.animations.rememberCalculatorHistoryAnimation
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase
import pe.edu.upc.calculator.features.calculator.presentation.CalculatorUiState
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorErrorRange
import pe.edu.upc.calculator.features.calculator.presentation.utils.buildCalculatorDisplayText

@Composable
fun CalculatorDisplay(
    state: CalculatorUiState,
    previewResult: (String) -> String?,
    onExpressionChange: (String, Int, Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    val display = state.display

    val formatExpressionUseCase = remember {
        FormatExpressionUseCase()
    }

    val formattedDisplay = formatExpressionUseCase(display)

    val displayLayout = createCalculatorDisplayLayout(formattedDisplay)
    val visualDisplay = displayLayout.visualDisplay

    val errorRange = if (
        state.error != null &&
        state.errorStartIndex != null &&
        state.errorEndIndex != null
    ) {
        calculatorErrorRange(
            expression = display,
            rawStart = state.errorStartIndex,
            rawEnd = state.errorEndIndex,
            displayLayout = displayLayout,
            formatExpressionUseCase = formatExpressionUseCase
        )
    } else {
        null
    }

    val displayText = buildCalculatorDisplayText(
        visualDisplay = visualDisplay,
        errorRange = errorRange
    )

    val showingResult = state.isResultDisplayed

    val historyAnimation = rememberCalculatorHistoryAnimation(
        historySelectionId = state.historySelectionId
    )

    val historyScale = historyAnimation.scale

    val targetFontSize = calculatorDisplayFontSize(
        showingResult = showingResult,
        displayLength = formattedDisplay.length
    )

    val displayFontSize = targetFontSize.value

    val animatedVisualFontSize = rememberCalculatorDisplayFontSize(
        targetFontSize = displayFontSize
    )

    CalculatorExpressionDisplay(
        display = display,
        formattedDisplay = formattedDisplay,
        displayText = displayText,
        displayLayout = displayLayout,
        cursorPosition = state.cursorPosition,
        formatExpressionUseCase = formatExpressionUseCase,
        fontSize = displayFontSize,
        modifier = modifier.graphicsLayer {
            val fontScale = animatedVisualFontSize / displayFontSize

            scaleX = historyScale.value * fontScale
            scaleY = historyScale.value * fontScale

            transformOrigin = TransformOrigin(
                pivotFractionX = 1f,
                pivotFractionY = 0.5f
            )
        },
        onExpressionChange = onExpressionChange
    )

    CalculatorDisplayPreview(
        display = display,
        showingResult = showingResult,
        hasError = state.error != null,
        previewResult = previewResult,
        formatExpressionUseCase = formatExpressionUseCase
    )
}