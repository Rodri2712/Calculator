package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import pe.edu.upc.calculator.features.calculator.presentation.animations.rememberCalculatorButtonScale
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorButtonBackgroundColor
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorButtonTextColor
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorButtonTextSize
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorButtonFontWeight

enum class ButtonStyle {
    Number,
    Operator,
    Equals
}

@Composable
fun CalculatorButton(
    label: String,
    style: ButtonStyle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale = rememberCalculatorButtonScale(isPressed)

    val backgroundColor = calculatorButtonBackgroundColor(style)

    val textColor = calculatorButtonTextColor(style)

    val textSize = calculatorButtonTextSize(label)

    val fontWeight = calculatorButtonFontWeight(style)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = Color.Transparent,
            interactionSource = interactionSource
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = label,
                    color = textColor,
                    fontSize = textSize,
                    fontWeight = fontWeight
                )
            }
        }
    }
}