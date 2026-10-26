package pe.edu.upc.calculator.features.calculator.presentation

import pe.edu.upc.calculator.features.calculator.application.EditOperatorUseCase
import javax.inject.Inject

class CalculatorOperatorHandler @Inject constructor(
    private val editOperator: EditOperatorUseCase
) {

    data class Result(
        val display: String,
        val cursorPosition: Int
    )

    fun handle(
        currentDisplay: String,
        currentCursorPosition: Int,
        operator: String
    ): Result? {
        val edited = editOperator(
            currentDisplay = currentDisplay,
            currentCursorPosition = currentCursorPosition,
            operator = operator
        ) ?: return null

        return Result(
            display = edited.display,
            cursorPosition = edited.cursorPosition
        )
    }
}