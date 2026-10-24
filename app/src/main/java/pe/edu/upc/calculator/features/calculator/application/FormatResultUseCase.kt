package pe.edu.upc.calculator.features.calculator.application

import java.math.BigDecimal
import javax.inject.Inject

class FormatResultUseCase @Inject constructor() {

    operator fun invoke(value: BigDecimal): String {
        val number = value.stripTrailingZeros()

        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "0"
        }

        val exponent = number.precision() - number.scale() - 1

        return if (
            exponent !in SMALL_NUMBER_EXPONENT_THRESHOLD + 1 until SCIENTIFIC_NOTATION_THRESHOLD
        ) {
            number.toString().replace("E", "e")
        } else {
            number.toPlainString()
        }
    }

    private companion object {
        const val SCIENTIFIC_NOTATION_THRESHOLD = 15
        const val SMALL_NUMBER_EXPONENT_THRESHOLD = -7
    }
}