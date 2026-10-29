package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.key
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorDisplayFontSize
import pe.edu.upc.calculator.features.calculator.presentation.utils.createCalculatorDisplayLayout
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

    val targetFontSize = calculatorDisplayFontSize(
        showingResult = showingResult,
        displayLength = formattedDisplay.length
    )

    val animatedFontSize = key(state.historySelectionId) {
        animateFloatAsState(
            targetValue = targetFontSize.value,
            animationSpec = tween(
                durationMillis = if (display.isEmpty()) 220 else 120,
                easing = FastOutSlowInEasing
            ),
            label = "CalculatorDisplayFontSize"
        )
    }

    CalculatorExpressionDisplay(
        display = display,
        formattedDisplay = formattedDisplay,
        displayText = displayText,
        displayLayout = displayLayout,
        cursorPosition = state.cursorPosition,
        formatExpressionUseCase = formatExpressionUseCase,
        fontSize = animatedFontSize.value,
        historySelectionId = state.historySelectionId,
        modifier = modifier,
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