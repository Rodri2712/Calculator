package pe.edu.upc.calculator.features.calculator.domain

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

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
        val value: BigDecimal,
        val isPercent: Boolean = false
    )

    fun parse(): BigDecimal {
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
                            left.value.add(
                                left.value.multiply(right.value)
                            )
                        } else {
                            left.value.add(right.value)
                        }
                    )
                }

                '-' -> {
                    position++
                    val right = parseTerm()

                    left = ParsedValue(
                        value = if (right.isPercent) {
                            left.value.subtract(
                                left.value.multiply(right.value)
                            )
                        } else {
                            left.value.subtract(right.value)
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

                    left = ParsedValue(
                        left.value.multiply(right.value)
                    )
                }

                '/' -> {
                    val operatorPosition = position
                    position++

                    val right = parseUnary()

                    if (right.value.compareTo(BigDecimal.ZERO) == 0) {
                        throw ExpressionParseException(
                            ExpressionError.DIVISION_BY_ZERO,
                            startIndex = operatorPosition,
                            endIndex = position
                        )
                    }

                    left = ParsedValue(
                        left.value.divide(
                            right.value,
                            DIVISION_CONTEXT
                        )
                    )
                }

                '(' -> {
                    val right = parseUnary()

                    left = ParsedValue(
                        left.value.multiply(right.value)
                    )
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
                    value = value.value.negate(),
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
                value = result.value.divide(ONE_HUNDRED),
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
        var digitsFound = 0

        while (position < input.length) {
            when (input[position]) {
                in '0'..'9' -> {
                    digitsFound++
                    position++
                }

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

        if (digitsFound == 0) {
            throw ExpressionParseException(
                if (decimalFound) {
                    ExpressionError.INVALID_DECIMAL
                } else {
                    ExpressionError.EXPECTED_NUMBER
                },
                startIndex = start,
                endIndex = (position + 1).coerceAtMost(input.length)
            )
        }

        if (position < input.length &&
            (input[position] == 'e' || input[position] == 'E')
        ) {
            position++

            if (position < input.length &&
                (input[position] == '+' || input[position] == '-')
            ) {
                position++
            }

            val exponentStart = position

            while (position < input.length &&
                input[position] in '0'..'9'
            ) {
                position++
            }

            if (position == exponentStart) {
                throw ExpressionParseException(
                    ExpressionError.INVALID_NUMBER,
                    startIndex = start,
                    endIndex = position
                )
            }
        }

        val number = input.substring(start, position)

        return try {
            ParsedValue(BigDecimal(number))
        } catch (_: NumberFormatException) {
            throw ExpressionParseException(
                ExpressionError.INVALID_NUMBER,
                startIndex = start,
                endIndex = position
            )
        }
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

    private companion object {
        val ONE_HUNDRED = BigDecimal("100")

        val DIVISION_CONTEXT = MathContext(
            16,
            RoundingMode.HALF_EVEN
        )
    }
}