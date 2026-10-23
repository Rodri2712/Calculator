package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class ToggleNumberSignUseCase @Inject constructor() {

    operator fun invoke(
        expression: String,
        cursor: Int
    ): Pair<String, Int> {
        if (expression.isEmpty() || expression == "Error") {
            return "0" to 1
        }

        val safeCursor = cursor.coerceIn(0, expression.length)

        val numbers = Regex("""\d+(?:\.\d*)?|\.\d+""")
            .findAll(expression)
            .toList()

        if (numbers.isEmpty()) {
            return expression to safeCursor
        }

        val selected = numbers.firstOrNull {
            safeCursor >= it.range.first &&
                    safeCursor <= it.range.last + 1
        } ?: numbers.last()

        val start = selected.range.first
        val end = selected.range.last + 1

        val minusIndex = start - 1

        val hasUnaryMinus = minusIndex >= 0 &&
                expression[minusIndex] == '−' &&
                (
                        minusIndex == 0 ||
                                expression[minusIndex - 1] in
                                listOf('+', '−', '×', '÷', '(')
                        )

        if (hasUnaryMinus) {
            val hasParentheses = minusIndex > 0 &&
                    expression[minusIndex - 1] == '(' &&
                    end < expression.length &&
                    expression[end] == ')'

            val replaceStart = if (hasParentheses) {
                minusIndex - 1
            } else {
                minusIndex
            }

            val replaceEnd = if (hasParentheses) {
                end + 1
            } else {
                end
            }

            val updated = expression.replaceRange(
                replaceStart,
                replaceEnd,
                expression.substring(start, end)
            )

            return updated to (replaceStart + end - start)
        }

        val alreadyWrapped = start > 0 &&
                expression[start - 1] == '(' &&
                end < expression.length &&
                expression[end] == ')'

        if (alreadyWrapped) {
            val updated = expression.substring(0, start) +
                    "−" +
                    expression.substring(start)

            return updated to (end + 1)
        }

        val previousIndex = (start - 1 downTo 0)
            .firstOrNull { !expression[it].isWhitespace() }

        val previous = previousIndex?.let { expression[it] }

        val needsParentheses = previous in
                listOf('+', '−', '×', '÷')

        val replacement = if (needsParentheses) {
            "(−${expression.substring(start, end)})"
        } else {
            "−${expression.substring(start, end)}"
        }

        val updated = expression.replaceRange(
            start,
            end,
            replacement
        )

        return updated to (start + replacement.length)
    }
}