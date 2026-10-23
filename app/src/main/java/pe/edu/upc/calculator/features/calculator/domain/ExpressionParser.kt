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
    INVALID_NUMBER
}

class ExpressionParseException(
    val error: ExpressionError,
    val startIndex: Int? = null,
    val endIndex: Int? = null
) : IllegalArgumentException()

class ExpressionParser(expression: String) {

    private val input = expression
        .replace("×", "*")
        .replace("÷", "/")
        .replace("−", "-")
        .replace(" ", "")

    private var position = 0

    private data class ParsedValue(
        val value: Double,
        val isPercent: Boolean = false
    )

    fun parse(): Double {
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

        val result = parseExpression()

        if (position != input.length) {
            throw ExpressionParseException(
                ExpressionError.INVALID_NUMBER,
                startIndex = position.coerceAtMost(input.length - 1).coerceAtLeast(0),
                endIndex = input.length
            )
        }

        if (!result.value.isFinite()) {
            throw ExpressionParseException(
                ExpressionError.INVALID_RESULT,
                startIndex = 0,
                endIndex = input.length
            )
        }

        return result.value
    }

    private fun parseExpression(): ParsedValue {
        var left = parseTerm()

        while (position < input.length) {
            when (input[position]) {
                '+' -> {
                    position++
                    val right = parseTerm()

                    left = ParsedValue(
                        value = if (right.isPercent) {
                            left.value + left.value * right.value
                        } else {
                            left.value + right.value
                        }
                    )
                }

                '-' -> {
                    position++
                    val right = parseTerm()

                    left = ParsedValue(
                        value = if (right.isPercent) {
                            left.value - left.value * right.value
                        } else {
                            left.value - right.value
                        }
                    )
                }

                else -> return left
            }
        }

        return left
    }

    private fun parseTerm(): ParsedValue {
        var left = parseUnary()

        while (position < input.length) {
            when (input[position]) {
                '*' -> {
                    position++
                    val right = parseUnary()

                    left = ParsedValue(left.value * right.value)
                }

                '/' -> {
                    val operatorPosition = position
                    position++

                    val right = parseUnary()

                    if (right.value == 0.0) {
                        throw ExpressionParseException(
                            ExpressionError.DIVISION_BY_ZERO,
                            startIndex = operatorPosition,
                            endIndex = position
                        )
                    }

                    left = ParsedValue(left.value / right.value)
                }

                '(' -> {
                    val right = parseUnary()
                    left = ParsedValue(left.value * right.value)
                }

                else -> return left
            }
        }

        return left
    }

    private fun parseUnary(): ParsedValue {
        return when {
            consume('+') -> parseUnary()

            consume('-') -> {
                val value = parseUnary()

                ParsedValue(
                    value = -value.value,
                    isPercent = value.isPercent
                )
            }

            else -> parsePostfix()
        }
    }

    private fun parsePostfix(): ParsedValue {
        var result = parsePrimary()

        if (consume('%')) {
            result = ParsedValue(
                value = result.value / 100.0,
                isPercent = true
            )

            if (position < input.length && input[position] == '%') {
                throw ExpressionParseException(
                    ExpressionError.INVALID_PERCENTAGE,
                    startIndex = position - 1,
                    endIndex = position + 1
                )
            }
        }

        return result
    }

    private fun parsePrimary(): ParsedValue {
        val openingPosition = position

        if (consume('(')) {
            val value = parseExpression()

            if (!consume(')')) {
                throw ExpressionParseException(
                    ExpressionError.UNCLOSED_PARENTHESIS,
                    startIndex = openingPosition,
                    endIndex = openingPosition + 1
                )
            }

            return value
        }

        val start = position
        var decimalFound = false

        while (position < input.length) {
            when (input[position]) {
                in '0'..'9' -> position++

                '.' -> {
                    if (decimalFound) {
                        throw ExpressionParseException(
                            ExpressionError.INVALID_DECIMAL,
                            startIndex = start,
                            endIndex = position + 1
                        )
                    }

                    decimalFound = true
                    position++
                }

                else -> break
            }
        }

        if (start >= position) {
            throw ExpressionParseException(
                ExpressionError.EXPECTED_NUMBER,
                startIndex = position,
                endIndex = (position + 1).coerceAtMost(input.length)
            )
        }

        val number = input.substring(start, position)

        if (number == ".") {
            throw ExpressionParseException(
                ExpressionError.INVALID_DECIMAL,
                startIndex = start,
                endIndex = position
            )
        }

        return ParsedValue(number.toDouble())
    }

    private fun consume(char: Char): Boolean {
        if (
            position < input.length &&
            input[position] == char
        ) {
            position++
            return true
        }

        return false
    }
}