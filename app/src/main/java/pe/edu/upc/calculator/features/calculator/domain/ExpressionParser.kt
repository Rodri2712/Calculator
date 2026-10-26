package pe.edu.upc.calculator.features.calculator.domain

import java.math.BigDecimal

class ExpressionParser(
    expression: String
) {

    private val input = ExpressionInputNormalizer()
        .normalize(expression)

    private val numberReader = ExpressionNumberReader(input)

    private val inputValidator = ExpressionInputValidator()

    private val resultValidator = ExpressionResultValidator()

    private val parenthesisValidator = ExpressionParenthesisValidator()

    private var position = 0

    private val percentageCalculator = ExpressionPercentageCalculator()

    private val divisionCalculator = ExpressionDivisionCalculator()

    private val postfixCalculator =
        ExpressionPostfixCalculator(percentageCalculator)

    fun parse(): BigDecimal {
        inputValidator.validate(input)

        val result = parseExpression()

        resultValidator.validate(
            input = input,
            position = position
        )

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
                        value = percentageCalculator.add(left.value, right)
                    )
                }

                '-' -> {
                    position++
                    val right = parseTerm()

                    left = ParsedValue(
                        value = percentageCalculator.subtract(left.value, right)
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

                    left = ParsedValue(
                        divisionCalculator.divide(
                            left = left.value,
                            right = right.value,
                            operatorPosition = operatorPosition,
                            endPosition = position
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
            result = postfixCalculator.applyPercent(result)

            if (position < input.length && input[position] == '%') {
                postfixCalculator.validatePercent(position)
            }
        }

        return result
    }

    private fun parsePrimary(): ParsedValue {
        val openingPosition = position

        if (consume('(')) {
            val value = parseExpression()

            val closingParenthesisFound = consume(')')

            parenthesisValidator.validateClosingParenthesis(
                openingPosition = openingPosition,
                closingParenthesisFound = closingParenthesisFound
            )

            return value
        }

        val parsedNumber = numberReader.read(position)

        position = parsedNumber.endPosition

        return ParsedValue(parsedNumber.value)
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