package pe.edu.upc.calculator.features.calculator.presentation

import javax.inject.Inject
class CalculatorDigitLimitValidator @Inject constructor() {

    fun exceedsLimit(
        display: String,
        cursorPosition: Int,
        limit: Int = 15
    ): Boolean {
        val cursor = cursorPosition.coerceIn(0, display.length)

        val numberAtCursor = Regex("""\d+(?:\.\d*)?|\.\d+""")
            .findAll(display)
            .firstOrNull { number ->
                cursor > number.range.first &&
                        cursor <= number.range.last + 1
            }

        val digitCount = numberAtCursor?.value?.count { it.isDigit() } ?: 0

        return digitCount > limit
    }
}