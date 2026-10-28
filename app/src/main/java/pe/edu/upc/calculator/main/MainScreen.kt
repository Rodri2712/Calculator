package pe.edu.upc.calculator.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pe.edu.upc.calculator.features.calculator.presentation.CalculatorScreen
import pe.edu.upc.calculator.features.calculator.presentation.CalculatorViewModel

@Composable
fun MainScreen(
    calculatorViewModel: CalculatorViewModel
) {
    val state by calculatorViewModel.state.collectAsState()

    CalculatorScreen(
        state = state,
        onDigitClick = calculatorViewModel::onDigitClick,
        onOperatorClick = calculatorViewModel::onOperatorClick,
        onClearClick = calculatorViewModel::onClearClick,
        onBackspaceClick = calculatorViewModel::onBackspaceClick,
        onExpressionChange = calculatorViewModel::onExpressionChange,
        onEqualsClick = calculatorViewModel::onEqualsClick,
        onHistoryClick = calculatorViewModel::toggleHistory,
        onClearHistory = calculatorViewModel::clearHistory,
        onHistoryItemClick = calculatorViewModel::selectHistoryItem,
        previewResult = calculatorViewModel::previewResult,
    )
}