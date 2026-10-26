package pe.edu.upc.calculator.features.calculator.domain

import java.math.BigDecimal

internal data class ParsedNumber(
    val value: BigDecimal,
    val endPosition: Int
)

internal class ExpressionNumberReader(
    private val input: String
) {

    fun read(start: Int): ParsedNumber {
        var position = start
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

        val value = try {
            BigDecimal(number)
        } catch (_: NumberFormatException) {
            throw ExpressionParseException(
                ExpressionError.INVALID_NUMBER,
                startIndex = start,
                endIndex = position
            )
        }

        return ParsedNumber(
            value = value,
            endPosition = position
        )
    }
}