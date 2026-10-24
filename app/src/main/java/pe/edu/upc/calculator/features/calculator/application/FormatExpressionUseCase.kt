package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class FormatExpressionUseCase @Inject constructor() {

    operator fun invoke(expression: String): String {
        return numberPattern.replace(expression) { match ->
            formatNumber(match.value)
        }
    }

    fun formattedPosition(
        expression: String,
        rawPosition: Int
    ): Int {
        val safePosition = rawPosition.coerceIn(0, expression.length)
        val formattedExpression = invoke(expression)

        if (safePosition == 0) {
            return 0
        }

        var rawCharactersFound = 0

        for (index in formattedExpression.indices) {
            if (formattedExpression[index] != ',') {
                rawCharactersFound++

                if (rawCharactersFound == safePosition) {
                    return index + 1
                }
            }
        }

        return formattedExpression.length
    }

    fun rawPosition(
        expression: String,
        formattedPosition: Int
    ): Int {
        val formattedExpression = invoke(expression)
        val targetPosition = formattedPosition.coerceIn(
            0,
            formattedExpression.length
        )

        var closestPosition = 0
        var smallestDistance = Int.MAX_VALUE

        for (rawPosition in 0..expression.length) {
            val visiblePosition = formattedPosition(
                expression = expression,
                rawPosition = rawPosition
            )

            val distance = kotlin.math.abs(
                visiblePosition - targetPosition
            )

            if (distance < smallestDistance) {
                smallestDistance = distance
                closestPosition = rawPosition
            }
        }

        return closestPosition
    }

    private fun formatNumber(number: String): String {
        val decimalIndex = number.indexOf('.')

        val integerPart = if (decimalIndex >= 0) {
            number.substring(0, decimalIndex)
        } else {
            number
        }

        val decimalPart = if (decimalIndex >= 0) {
            number.substring(decimalIndex)
        } else {
            ""
        }

        val groupedInteger = integerPart
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()

        return groupedInteger + decimalPart
    }

    private companion object {
        val numberPattern = Regex("""\d+(?:\.\d*)?""")
    }
}