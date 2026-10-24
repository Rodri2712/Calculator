package pe.edu.upc.calculator.features.calculator.application

import java.math.BigDecimal
import javax.inject.Inject

class FormatResultUseCase @Inject constructor() {

    operator fun invoke(value: Double): String {
        val number = BigDecimal.valueOf(value)
            .stripTrailingZeros()

        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "0"
        }

        val exponent = number.precision() - number.scale() - 1

        return if (
            exponent in SCIENTIFIC_NOTATION_THRESHOLD..Int.MAX_VALUE ||
            exponent in Int.MIN_VALUE..SMALL_NUMBER_EXPONENT_THRESHOLD
        ) {
            number.toString()
                .replace("E", "e")
                .let { scientific ->
                    if ("e" in scientific) {
                        val exponentIndex = scientific.indexOf('e')
                        val exponentPart = scientific.substring(exponentIndex + 1)

                        if (
                            exponentPart.startsWith("-") ||
                            exponentPart.startsWith("+")
                        ) {
                            scientific
                        } else {
                            scientific.substring(0, exponentIndex) +
                                    "e+" + exponentPart
                        }
                    } else {
                        scientific
                    }
                }
        } else {
            number.toPlainString()
        }
    }

    private companion object {
        const val SCIENTIFIC_NOTATION_THRESHOLD = 15
        const val SMALL_NUMBER_EXPONENT_THRESHOLD = -7
    }
}