package pe.edu.upc.calculator.features.calculator.domain

internal class ExpressionPostfixCalculator(
    private val percentageCalculator: ExpressionPercentageCalculator
) {

    fun applyPercent(value: ParsedValue): ParsedValue {
        return ParsedValue(
            value = percentageCalculator.toFraction(value.value),
            isPercent = true
        )
    }

    fun validatePercent(position: Int) {
        throw ExpressionParseException(
            ExpressionError.INVALID_PERCENTAGE,
            startIndex = position - 1,
            endIndex = position + 1
        )
    }
}