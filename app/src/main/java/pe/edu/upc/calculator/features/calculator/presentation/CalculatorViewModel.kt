package pe.edu.upc.calculator.features.calculator.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.application.PreviewExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.GetHistoryUseCase
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.features.calculator.domain.ExpressionError

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val resultHandler: CalculatorResultHandler,
    private val historyHandler: CalculatorHistoryHandler,
    getHistory: GetHistoryUseCase,
    private val previewExpression: PreviewExpressionUseCase,
    private val digitHandler: CalculatorDigitHandler,
    private val operatorHandler: CalculatorOperatorHandler,
    private val editingHandler: CalculatorEditingHandler,
) : ViewModel() {

    private val _state = MutableStateFlow(
        CalculatorUiState(history = getHistory())
    )

    val state: StateFlow<CalculatorUiState> = _state.asStateFlow()

    private var justCalculated = false

    fun onCursorPositionChange(position: Int) {
        _state.update { current ->
            val newPosition = position.coerceIn(
                0,
                current.display.length
            )

            if (current.cursorPosition == newPosition) {
                current
            } else {
                current.copy(cursorPosition = newPosition)
            }
        }
    }

    fun onDigitClick(value: String) {
        _state.update { current ->
            val result = digitHandler.handle(
                currentDisplay = current.display,
                currentCursorPosition = current.cursorPosition,
                value = value,
                justCalculated = justCalculated
            )

            if (result.exceedsDigitLimit) {
                return@update current.copy(
                    error = ExpressionError.DIGIT_LIMIT,
                    errorToastId = current.errorToastId + 1
                )
            }

            current.copy(
                display = result.display,
                cursorPosition = result.cursorPosition,
                isResultDisplayed = false,
                error = null,
                errorStartIndex = null,
                errorEndIndex = null
            )
        }

        justCalculated = false
    }

    fun onOperatorClick(operator: String) {
        var operatorHandled = false

        _state.update { current ->
            val result = operatorHandler.handle(
                currentDisplay = current.display,
                currentCursorPosition = current.cursorPosition,
                operator = operator
            )

            if (result == null) {
                current
            } else {
                operatorHandled = true

                current.copy(
                    display = result.display,
                    cursorPosition = result.cursorPosition,
                    isResultDisplayed = false,
                    error = null,
                    errorStartIndex = null,
                    errorEndIndex = null
                )
            }
        }

        if (operatorHandled) {
            justCalculated = false
        }
    }
    fun onClearClick() {
        justCalculated = false

        _state.update { current ->
            val result = editingHandler.clear()

            current.copy(
                display = result.display,
                cursorPosition = result.cursorPosition,
                isResultDisplayed = false,
                error = null,
                errorStartIndex = null,
                errorEndIndex = null
            )
        }
    }

    fun onBackspaceClick() {
        justCalculated = false

        _state.update { current ->
            val result = editingHandler.backspace(
                currentDisplay = current.display,
                currentCursorPosition = current.cursorPosition
            )

            current.copy(
                display = result.display,
                cursorPosition = result.cursorPosition,
                isResultDisplayed = false,
                error = null,
                errorStartIndex = null,
                errorEndIndex = null
            )
        }
    }

    fun onEqualsClick() {
        if (justCalculated) return

        val expression = _state.value.display
        val result = resultHandler.calculate(expression)

        if (result.formattedResult != null) {
            val formattedResult = result.formattedResult

            _state.update { current ->
                current.copy(
                    display = formattedResult,
                    cursorPosition = formattedResult.length,
                    history = historyHandler.addItem(
                        operation = expression,
                        result = formattedResult,
                        currentHistory = current.history
                    ),
                    isResultDisplayed = true,
                    error = null,
                    errorStartIndex = null,
                    errorEndIndex = null
                )
            }

            historyHandler.save(_state.value.history)
            justCalculated = true
        } else {
            _state.update { current ->
                current.copy(
                    display = expression,
                    cursorPosition = current.cursorPosition.coerceIn(
                        0,
                        expression.length
                    ),
                    isResultDisplayed = false,
                    error = result.error,
                    errorToastId = current.errorToastId + 1,
                    errorStartIndex = result.errorStartIndex,
                    errorEndIndex = result.errorEndIndex
                )
            }

            justCalculated = false
        }
    }

    fun toggleHistory() {
        _state.update {
            it.copy(isHistoryVisible = !it.isHistoryVisible)
        }
    }

    fun clearHistory() {
        historyHandler.clear()

        _state.update { current ->
            current.copy(history = emptyList())
        }
    }

    fun selectHistoryItem(item: HistoryItem) {
        justCalculated = false

        _state.update {
            it.copy(
                display = item.operation,
                cursorPosition = item.operation.length,
                isHistoryVisible = false,
                isResultDisplayed = false,
                error = null,
                errorStartIndex = null,
                errorEndIndex = null,
                historySelectionId = it.historySelectionId + 1
            )
        }
    }

    fun previewResult(expression: String): String? {
        return previewExpression(expression)
    }
}