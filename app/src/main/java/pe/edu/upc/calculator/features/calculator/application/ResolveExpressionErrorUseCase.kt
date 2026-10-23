package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.ExpressionError
import pe.edu.upc.calculator.features.calculator.domain.ExpressionParseException

data class ResolvedExpressionError(
    val error: ExpressionError,
    val startIndex: Int?,
    val endIndex: Int?
)

class ResolveExpressionErrorUseCase @Inject constructor() {

    operator fun invoke(
        expression: String,
        exception: Exception
    ): ResolvedExpressionError {
        val parseException = exception as? ExpressionParseException

        val error = parseException?.error
            ?: ExpressionError.INVALID_RESULT

        val visibleIndices = expression.indices.filterNot {
            expression[it].isWhitespace()
        }

        val startIndex = parseException?.startIndex?.let { index ->
            (visibleIndices.getOrNull(index) ?: expression.length)
                .coerceIn(0, expression.length)
        }

        val endIndex = parseException?.endIndex?.let { index ->
            val originalEnd = if (index <= 0) {
                0
            } else {
                visibleIndices.getOrNull(index - 1)?.plus(1)
                    ?: expression.length
            }

            originalEnd.coerceIn(0, expression.length)
        }

        return ResolvedExpressionError(
            error = error,
            startIndex = startIndex,
            endIndex = endIndex
        )
    }
}