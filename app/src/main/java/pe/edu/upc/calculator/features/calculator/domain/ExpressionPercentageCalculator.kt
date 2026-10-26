package pe.edu.upc.calculator.features.calculator.domain

import java.math.BigDecimal

internal class ExpressionPercentageCalculator {

    fun add(left: BigDecimal, right: ParsedValue): BigDecimal {
        val adjustedRight = if (right.isPercent) {
            left.multiply(right.value)
        } else {
            right.value
        }

        return left.add(adjustedRight)
    }

    fun subtract(left: BigDecimal, right: ParsedValue): BigDecimal {
        val adjustedRight = if (right.isPercent) {
            left.multiply(right.value)
        } else {
            right.value
        }

        return left.subtract(adjustedRight)
    }

    fun toFraction(value: BigDecimal): BigDecimal {
        return value.divide(ONE_HUNDRED)
    }

    private companion object {
        val ONE_HUNDRED = BigDecimal("100")
    }
}