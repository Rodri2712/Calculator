package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorButtonRows
import pe.edu.upc.calculator.features.calculator.presentation.utils.handleCalculatorButtonClick

@Composable
fun CalculatorKeypad(
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onEqualsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonRows = calculatorButtonRows()

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
                            handleCalculatorButtonClick(
                                label = button.label,
                                onDigitClick = onDigitClick,
                                onOperatorClick = onOperatorClick,
                                onClearClick = onClearClick,
                                onEqualsClick = onEqualsClick
                            )
                        }
                    )
                }
            }
        }
    }
}