package pe.edu.upc.calculator.features.calculator.presentation.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun rememberCalculatorButtonScale(
    isPressed: Boolean
): Float {
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = tween(
            durationMillis = if (isPressed) 80 else 140,
            easing = FastOutSlowInEasing
        ),
        label = "button_press_scale"
    )

    return scale
}