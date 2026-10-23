package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject

class ClearExpressionUseCase @Inject constructor() {

    data class Result(
        val display: String,
        val cursorPosition: Int
    )

    operator fun invoke(): Result {
        return Result(
            display = "",
            cursorPosition = 0
        )
    }
}