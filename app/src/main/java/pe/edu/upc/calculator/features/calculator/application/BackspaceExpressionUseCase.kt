package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class BackspaceExpressionUseCase @Inject constructor() {

    data class Result(
        val display: String,
        val cursorPosition: Int
    )

    operator fun invoke(
        currentDisplay: String,
        currentCursorPosition: Int
    ): Result {
        val cursor = currentCursorPosition.coerceIn(
            0,
            currentDisplay.length
        )

        if (currentDisplay == "Error" || currentDisplay == "0") {
            return Result(
                display = "",
                cursorPosition = 0
            )
        }

        if (currentDisplay.isEmpty() || cursor == 0) {
            return Result(
                display = currentDisplay,
                cursorPosition = cursor
            )
        }

        val newDisplay = when {
            currentDisplay.length <= 1 -> ""
            currentDisplay == "−0" -> ""
            currentDisplay.length == 2 &&
                    currentDisplay.startsWith("−") -> ""
            else -> currentDisplay.removeRange(cursor - 1, cursor)
        }

        val newCursor = (cursor - 1).coerceIn(
            0,
            newDisplay.length
        )

        return Result(
            display = newDisplay,
            cursorPosition = newCursor
        )
    }
}