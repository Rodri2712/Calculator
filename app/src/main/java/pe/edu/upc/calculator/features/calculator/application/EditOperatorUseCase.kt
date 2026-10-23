package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class EditOperatorUseCase @Inject constructor() {

    data class Result(
        val display: String,
        val cursorPosition: Int
    )

    operator fun invoke(
        currentDisplay: String,
        currentCursorPosition: Int,
        operator: String
    ): Result? {
        val symbol = when (operator) {
            "+" -> "+"
            "−", "-" -> "−"
            "×", "*" -> "×"
            "÷", "/" -> "÷"
            else -> return null
        }

        var display = currentDisplay
        var cursor = currentCursorPosition.coerceIn(
            0,
            display.length
        )

        if (display == "Error") {
            display = "0"
            cursor = 1
        }

        val before = display.substring(0, cursor)
        val after = display.substring(cursor)
        val previous = before.lastOrNull()

        when {
            display == "0" && cursor == 1 && symbol == "−" -> {
                display = "−"
                cursor = 1
            }

            display == "0" && cursor == 1 &&
                    (symbol == "×" || symbol == "÷") -> {
            }

            previous in listOf('+', '−', '×', '÷') -> {
                display = before.dropLast(1) + symbol + after
            }

            previous == '(' && symbol != "−" -> {
            }

            else -> {
                val insertion = if (cursor == 0 && symbol != "−") {
                    "0$symbol"
                } else {
                    symbol
                }

                display = before + insertion + after
                cursor += insertion.length
            }
        }

        return Result(
            display = display,
            cursorPosition = cursor.coerceIn(0, display.length)
        )
    }
}