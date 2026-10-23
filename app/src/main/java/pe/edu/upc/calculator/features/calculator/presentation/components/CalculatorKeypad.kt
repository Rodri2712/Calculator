package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class CalcButton(
    val label: String,
    val style: ButtonStyle
)

@Composable
fun CalculatorKeypad(
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onEqualsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonRows = listOf(
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(Modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { button ->
                    CalculatorButton(
                        label = button.label,
                        style = button.style,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = {
                            when (button.label) {
                                "C" -> onClearClick()
                                "=" -> onEqualsClick()
                                "÷", "×", "−", "+" ->
                                    onOperatorClick(button.label)
                                else -> onDigitClick(button.label)
                            }
                        }
                    )
                }
            }
        }
    }
}