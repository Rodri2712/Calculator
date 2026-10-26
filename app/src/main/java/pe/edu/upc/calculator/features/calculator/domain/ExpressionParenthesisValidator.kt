package pe.edu.upc.calculator.features.calculator.domain

internal class ExpressionParenthesisValidator {

    fun validateClosingParenthesis(
        openingPosition: Int,
        closingParenthesisFound: Boolean
    ) {
        if (!closingParenthesisFound) {
            throw ExpressionParseException(
                ExpressionError.UNCLOSED_PARENTHESIS,
                startIndex = openingPosition,
                endIndex = openingPosition + 1
            )
        }
    }
}