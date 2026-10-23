package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class EvaluateExpressionUseCase @Inject constructor(
    private val calculateExpression: CalculateExpressionUseCase,
    private val formatResult: FormatResultUseCase
) {

    operator fun invoke(expression: String): String {
        val result = calculateExpression(expression)
        return formatResult(result)
    }
}