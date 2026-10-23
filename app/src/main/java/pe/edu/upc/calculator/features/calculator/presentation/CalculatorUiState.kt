package pe.edu.upc.calculator.features.calculator.presentation

import pe.edu.upc.calculator.features.calculator.domain.ExpressionError
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem

data class CalculatorUiState(
    val display: String = "0",
    val history: List<HistoryItem> = emptyList(),
    val isHistoryVisible: Boolean = false,
    val isResultDisplayed: Boolean = false,
    val cursorPosition: Int = display.length,
    val error: ExpressionError? = null,
    val errorToastId: Int = 0,
    val errorStartIndex: Int? = null,
    val errorEndIndex: Int? = null
)