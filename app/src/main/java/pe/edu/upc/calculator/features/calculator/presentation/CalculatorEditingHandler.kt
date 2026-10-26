package pe.edu.upc.calculator.features.calculator.presentation

import pe.edu.upc.calculator.features.calculator.application.BackspaceExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.ClearExpressionUseCase
import javax.inject.Inject

class CalculatorEditingHandler @Inject constructor(
    private val backspaceExpression: BackspaceExpressionUseCase,
    private val clearExpression: ClearExpressionUseCase
) {

    data class Result(
        val display: String,
        val cursorPosition: Int
    )

    fun clear(): Result {
        val result = clearExpression()

        return Result(
            display = result.display,
            cursorPosition = result.cursorPosition
        )
    }

    fun backspace(
        currentDisplay: String,
        currentCursorPosition: Int
    ): Result {
        val result = backspaceExpression(
            currentDisplay = currentDisplay,
            currentCursorPosition = currentCursorPosition
        )

        return Result(
            display = result.display,
            cursorPosition = result.cursorPosition
        )
    }
}