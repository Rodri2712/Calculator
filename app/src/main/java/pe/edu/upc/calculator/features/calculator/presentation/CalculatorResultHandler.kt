package pe.edu.upc.calculator.features.calculator.presentation

import pe.edu.upc.calculator.features.calculator.application.EvaluateExpressionUseCase
import pe.edu.upc.calculator.features.calculator.application.ResolveExpressionErrorUseCase
import javax.inject.Inject

class CalculatorResultHandler @Inject constructor(
    private val evaluateExpression: EvaluateExpressionUseCase,
    private val resolveExpressionError: ResolveExpressionErrorUseCase
) {

    data class Result(
        val formattedResult: String? = null,
        val error: pe.edu.upc.calculator.features.calculator.domain.ExpressionError? = null,
        val errorStartIndex: Int? = null,
        val errorEndIndex: Int? = null
    )

    fun calculate(expression: String): Result {
        return try {
            Result(formattedResult = evaluateExpression(expression))
        } catch (exception: Exception) {
            val resolvedError = resolveExpressionError(
                expression = expression,
                exception = exception
            )

            Result(
                error = resolvedError.error,
                errorStartIndex = resolvedError.startIndex,
                errorEndIndex = resolvedError.endIndex
            )
        }
    }
}