package pe.edu.upc.calculator.features.calculator.presentation.utils

data class CalculatorDisplayLayout(
    val formattedDisplay: String,
    val visualDisplay: String,
    val lineBreakPositions: List<Int>
) {
    fun toVisualPosition(position: Int): Int {
        val safePosition = position.coerceIn(0, formattedDisplay.length)

        return safePosition + lineBreakPositions.count { it <= safePosition }
    }

    fun toFormattedPosition(position: Int): Int {
        val safePosition = position.coerceIn(0, visualDisplay.length)

        return visualDisplay
            .take(safePosition)
            .count { it != '\n' }
            .coerceIn(0, formattedDisplay.length)
    }
}

fun createCalculatorDisplayLayout(
    formattedDisplay: String
): CalculatorDisplayLayout {
    val lineBreakPositions = if (formattedDisplay.length > 24) {
        formattedDisplay.indices.filter { index ->
            index > 0 &&
                    formattedDisplay[index] in listOf('+', '-', '−', '×', '÷') &&
                    (
                            formattedDisplay[index - 1].isDigit() ||
                                    formattedDisplay[index - 1] == ')' ||
                                    formattedDisplay[index - 1] == '%'
                            )
        }
    } else {
        emptyList()
    }

    val visualDisplay = buildString {
        formattedDisplay.forEachIndexed { index, character ->
            if (index in lineBreakPositions) {
                append('\n')
            }
            append(character)
        }
    }

    return CalculatorDisplayLayout(
        formattedDisplay = formattedDisplay,
        visualDisplay = visualDisplay,
        lineBreakPositions = lineBreakPositions
    )
}