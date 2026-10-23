package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.ExpressionParser

class CalculateExpressionUseCase @Inject constructor() {

    operator fun invoke(expression: String): Double {
        return ExpressionParser(expression).parse()
    }
}