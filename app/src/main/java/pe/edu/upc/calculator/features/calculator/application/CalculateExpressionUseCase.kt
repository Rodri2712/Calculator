package pe.edu.upc.calculator.features.calculator.application

import java.math.BigDecimal
import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.ExpressionParser

class CalculateExpressionUseCase @Inject constructor() {

    operator fun invoke(expression: String): BigDecimal {
        return ExpressionParser(expression).parse()
    }
}