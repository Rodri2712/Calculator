package pe.edu.upc.calculator.features.calculator.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.application.AddHistoryItemUseCase
import pe.edu.upc.calculator.features.calculator.application.ResolveExpressionErrorUseCase
import pe.edu.upc.calculator.features.calculator.application.EvaluateExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.ClearExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.BackspaceExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.EditOperatorUseCase
import pe.edu.upc.calculator.features.calculator.application.EditExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.PreviewExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.ClearHistoryUseCase
import pe.edu.upc.calculator.features.calculator.application.GetHistoryUseCase
import pe.edu.upc.calculator.features.calculator.application.SaveHistoryUseCase
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.features.calculator.domain.ExpressionError

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val evaluateExpression: EvaluateExpressionUseCase,
    private val addHistoryItem: AddHistoryItemUseCase,
    private val resolveExpressionError: ResolveExpressionErrorUseCase,
    getHistory: GetHistoryUseCase,
    private val saveHistory: SaveHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase,
    private val previewExpression: PreviewExpressionUseCase,
    private val editExpression: EditExpressionUseCase,
    private val editOperator: EditOperatorUseCase,
    private val backspaceExpression: BackspaceExpressionUseCase,
    private val clearExpression: ClearExpressionUseCase,
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
            val result = editExpression(
                currentDisplay = current.display,
                currentCursorPosition = current.cursorPosition,
                value = value,
                justCalculated = justCalculated
            )

            val exceedsDigitLimit = Regex("""\d+(?:\.\d*)?|\.\d+""")
                .findAll(result.display)
                .any { number ->
                    number.value.count { it.isDigit() } > 15
                }

            if (exceedsDigitLimit) {
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
            val result = editOperator(
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
            val result = clearExpression()

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
            val result = backspaceExpression(
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

        try {
            val formattedResult = evaluateExpression(expression)

            _state.update { current ->
                current.copy(
                    display = formattedResult,
                    cursorPosition = formattedResult.length,
                    history = addHistoryItem(
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

            saveHistory(_state.value.history)
            justCalculated = true
        } catch (exception: Exception) {
            val resolvedError = resolveExpressionError(
                expression = expression,
                exception = exception
            )

            _state.update { current ->
                current.copy(
                    display = expression,
                    cursorPosition = current.cursorPosition.coerceIn(
                        0,
                        expression.length
                    ),
                    isResultDisplayed = false,
                    error = resolvedError.error,
                    errorToastId = current.errorToastId + 1,
                    errorStartIndex = resolvedError.startIndex,
                    errorEndIndex = resolvedError.endIndex
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
        clearHistoryUseCase()

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