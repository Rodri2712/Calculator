package pe.edu.upc.calculator.features.calculator.domain

internal class ExpressionResultValidator {

    fun validate(
        input: String,
        position: Int
    ) {
        if (position != input.length) {
            throw ExpressionParseException(
                ExpressionError.INVALID_NUMBER,
                startIndex = position.coerceAtMost(input.length - 1)
                    .coerceAtLeast(0),
                endIndex = input.length
            )
        }
    }
}