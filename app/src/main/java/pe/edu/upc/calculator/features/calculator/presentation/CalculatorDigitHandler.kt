package pe.edu.upc.calculator.features.calculator.presentation

import pe.edu.upc.calculator.features.calculator.application.EditExpressionUseCase
import javax.inject.Inject

class CalculatorDigitHandler @Inject constructor(
    private val editExpression: EditExpressionUseCase,
    private val digitLimitValidator: CalculatorDigitLimitValidator
) {

    data class Result(
        val display: String,
        val cursorPosition: Int,
        val exceedsDigitLimit: Boolean
    )

    fun handle(
        currentDisplay: String,
        currentCursorPosition: Int,
        value: String,
        justCalculated: Boolean
    ): Result {
        val edited = editExpression(
            currentDisplay = currentDisplay,
            currentCursorPosition = currentCursorPosition,
            value = value,
            justCalculated = justCalculated
        )

        return Result(
            display = edited.display,
            cursorPosition = edited.cursorPosition,
            exceedsDigitLimit = digitLimitValidator.exceedsLimit(
                display = edited.display,
                cursorPosition = edited.cursorPosition
            )
        )
    }
}