package pe.edu.upc.calculator.features.calculator.domain

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

internal class ExpressionDivisionCalculator {

    fun divide(
        left: BigDecimal,
        right: BigDecimal,
        operatorPosition: Int,
        endPosition: Int
    ): BigDecimal {
        if (right.compareTo(BigDecimal.ZERO) == 0) {
            throw ExpressionParseException(
                ExpressionError.DIVISION_BY_ZERO,
                startIndex = operatorPosition,
                endIndex = endPosition
            )
        }

        return left.divide(right, DIVISION_CONTEXT)
    }

    private companion object {
        val DIVISION_CONTEXT = MathContext(
            16,
            RoundingMode.HALF_EVEN
        )
    }
}