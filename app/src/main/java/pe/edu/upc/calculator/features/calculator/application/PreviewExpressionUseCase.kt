package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class PreviewExpressionUseCase @Inject constructor(
    private val calculateExpression: CalculateExpressionUseCase,
    private val formatResult: FormatResultUseCase
) {

    operator fun invoke(expression: String): String? {
        return try {
            formatResult(calculateExpression(expression))
        } catch (_: Exception) {
            null
        }
    }
}