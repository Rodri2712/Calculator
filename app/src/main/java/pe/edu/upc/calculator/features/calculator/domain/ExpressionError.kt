package pe.edu.upc.calculator.features.calculator.domain

enum class ExpressionError {
    EMPTY_EXPRESSION,
    INCOMPLETE_EXPRESSION,
    INVALID_RESULT,
    DIVISION_BY_ZERO,
    INVALID_PERCENTAGE,
    UNCLOSED_PARENTHESIS,
    INVALID_DECIMAL,
    EXPECTED_NUMBER,
    INVALID_NUMBER,
    DIGIT_LIMIT
}