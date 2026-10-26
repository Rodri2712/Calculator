package pe.edu.upc.calculator.features.calculator.domain

internal class ExpressionInputValidator {

    fun validate(input: String) {
        if (input.isBlank()) {
            throw ExpressionParseException(
                ExpressionError.EMPTY_EXPRESSION
            )
        }

        if (input.lastOrNull() in listOf('+', '-', '*', '/', '(')) {
            throw ExpressionParseException(
                ExpressionError.INCOMPLETE_EXPRESSION,
                startIndex = (input.length - 1).coerceAtLeast(0),
                endIndex = input.length
            )
        }
    }
}