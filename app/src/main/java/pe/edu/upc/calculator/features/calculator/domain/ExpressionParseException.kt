package pe.edu.upc.calculator.features.calculator.domain

class ExpressionParseException(
    val error: ExpressionError,
    val startIndex: Int? = null,
    val endIndex: Int? = null
) : IllegalArgumentException()