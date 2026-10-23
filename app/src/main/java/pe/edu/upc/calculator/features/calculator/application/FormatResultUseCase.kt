package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import java.math.BigDecimal

class FormatResultUseCase @Inject constructor() {

    operator fun invoke(value: Double): String {
        return BigDecimal.valueOf(value)
            .stripTrailingZeros()
            .toPlainString()
    }
}