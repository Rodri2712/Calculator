package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pe.edu.upc.calculator.core.designsystems.theme.EqualsButtonColor
import pe.edu.upc.calculator.core.designsystems.theme.NumberButtonColor
import pe.edu.upc.calculator.core.designsystems.theme.OperatorButtonColor

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

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = tween(
            durationMillis = if (isPressed) 80 else 140,
            easing = FastOutSlowInEasing
        ),
        label = "button_press_scale"
    )

    val backgroundColor = when (style) {
        ButtonStyle.Number -> NumberButtonColor
        ButtonStyle.Operator -> OperatorButtonColor
        ButtonStyle.Equals -> EqualsButtonColor
    }

    val textColor = when (style) {
        ButtonStyle.Number -> Color.White
        ButtonStyle.Operator -> Color.Black
        ButtonStyle.Equals -> Color.White
    }

    val textSize = when (label) {
        "( )", "+/−" -> 22.sp
        "÷", "×", "−", "+" -> 34.sp
        "=" -> 36.sp
        else -> 32.sp
    }

    val fontWeight = when (style) {
        ButtonStyle.Operator -> FontWeight.Medium
        else -> FontWeight.Normal
    }

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