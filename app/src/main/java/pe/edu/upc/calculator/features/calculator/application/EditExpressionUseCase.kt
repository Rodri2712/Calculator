package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class EditExpressionUseCase @Inject constructor(
    private val toggleNumberSign: ToggleNumberSignUseCase
) {

    data class Result(
        val display: String,
        val cursorPosition: Int
    )

    operator fun invoke(
        currentDisplay: String,
        currentCursorPosition: Int,
        value: String,
        justCalculated: Boolean
    ): Result {
        var display = currentDisplay
        var cursor = currentCursorPosition.coerceIn(0, display.length)

        if (display == "Error") {
            display = "0"
            cursor = 1
        }

        if (
            justCalculated &&
            (value.length == 1 && value[0].isDigit() || value == ".")
        ) {
            display = "0"
            cursor = 1
        }

        when (value) {
            in "0".."9" -> {
                val before = display.substring(0, cursor)
                val after = display.substring(cursor)

                when (display) {
                    "" -> {
                        display = value
                        cursor = display.length
                    }

                    "0" -> {
                        if (cursor == 1) {
                            display = value
                            cursor = display.length
                        } else {
                            display = value + "0"
                            cursor = 1
                        }
                    }

                    "−0" -> {
                        if (cursor == 2) {
                            display = "−$value"
                            cursor = display.length
                        } else {
                            val insertionPosition = cursor.coerceAtLeast(1)
                            display = display.substring(0, insertionPosition) +
                                    value +
                                    display.substring(insertionPosition)
                            cursor = insertionPosition + 1
                        }
                    }

                    else -> {
                        val insertion = when (before.lastOrNull()) {
                            ')', '%' -> "×$value"
                            else -> value
                        }

                        display = before + insertion + after
                        cursor += insertion.length
                    }
                }
            }

            "." -> {
                val before = display.substring(0, cursor)
                val after = display.substring(cursor)
                val currentNumber = before.takeLastWhile {
                    it.isDigit() || it == '.'
                }

                if ('.' !in currentNumber) {
                    val previous = before.lastOrNull()

                    val insertion = when {
                        before.isEmpty() && display == "0" -> "0."
                        before == "0" && cursor == display.length -> "0."
                        previous?.isDigit() == true -> "."
                        previous in listOf('+', '−', '×', '÷', '(') -> "0."
                        previous == ')' || previous == '%' -> "×0."
                        else -> "0."
                    }

                    if (display == "0" && cursor == 1) {
                        display = insertion
                        cursor = display.length
                    } else {
                        display = before + insertion + after
                        cursor += insertion.length
                    }
                }
            }

            "( )" -> {
                val before = display.substring(0, cursor)
                val after = display.substring(cursor)
                val opens = display.count { it == '(' }
                val closes = display.count { it == ')' }
                val previous = before.lastOrNull()

                val insertion = when {
                    display == "0" && cursor == 1 -> "("
                    opens > closes &&
                            (previous?.isDigit() == true ||
                                    previous == ')' ||
                                    previous == '%') -> ")"

                    previous?.isDigit() == true ||
                            previous == ')' ||
                            previous == '%' -> "×("

                    previous in listOf('+', '−', '×', '÷', '(') -> "("
                    else -> "("
                }

                if (display == "0" && cursor == 1) {
                    display = insertion
                    cursor = display.length
                } else {
                    display = before + insertion + after
                    cursor += insertion.length
                }
            }

            "%" -> {
                val before = display.substring(0, cursor)
                val after = display.substring(cursor)
                val previous = before.lastOrNull()

                if (previous?.isDigit() == true || previous == ')') {
                    display = "${before}%$after"
                    cursor++
                }
            }

            "+/−" -> {
                val result = toggleNumberSign(display, cursor)
                display = result.first
                cursor = result.second
            }
        }

        return Result(
            display = display,
            cursorPosition = cursor.coerceIn(0, display.length)
        )
    }
}