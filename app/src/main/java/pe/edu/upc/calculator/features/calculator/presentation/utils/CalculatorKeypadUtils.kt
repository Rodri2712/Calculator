package pe.edu.upc.calculator.features.calculator.presentation.utils

import pe.edu.upc.calculator.features.calculator.presentation.components.ButtonStyle

data class CalcButton(
    val label: String,
    val style: ButtonStyle
)

fun calculatorButtonRows(): List<List<CalcButton>> {
    return listOf(
        listOf(
            CalcButton("C", ButtonStyle.Number),
            CalcButton("( )", ButtonStyle.Number),
            CalcButton("%", ButtonStyle.Number),
            CalcButton("÷", ButtonStyle.Operator)
        ),
        listOf(
            CalcButton("7", ButtonStyle.Number),
            CalcButton("8", ButtonStyle.Number),
            CalcButton("9", ButtonStyle.Number),
            CalcButton("×", ButtonStyle.Operator)
        ),
        listOf(
            CalcButton("4", ButtonStyle.Number),
            CalcButton("5", ButtonStyle.Number),
            CalcButton("6", ButtonStyle.Number),
            CalcButton("−", ButtonStyle.Operator)
        ),
        listOf(
            CalcButton("1", ButtonStyle.Number),
            CalcButton("2", ButtonStyle.Number),
            CalcButton("3", ButtonStyle.Number),
            CalcButton("+", ButtonStyle.Operator)
        ),
        listOf(
            CalcButton("+/−", ButtonStyle.Number),
            CalcButton("0", ButtonStyle.Number),
            CalcButton(".", ButtonStyle.Number),
            CalcButton("=", ButtonStyle.Equals)
        )
    )
}

fun handleCalculatorButtonClick(
    label: String,
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onEqualsClick: () -> Unit
) {
    when (label) {
        "C" -> onClearClick()
        "=" -> onEqualsClick()
        "÷", "×", "−", "+" -> onOperatorClick(label)
        else -> onDigitClick(label)
    }
}