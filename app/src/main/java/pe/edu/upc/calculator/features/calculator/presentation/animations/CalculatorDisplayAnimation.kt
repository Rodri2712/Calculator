package pe.edu.upc.calculator.features.calculator.presentation.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberCalculatorDisplayScale(
    targetFontSize: Float
): Float {
    val previousFontSize = remember {
        mutableFloatStateOf(targetFontSize)
    }

    val animatedScale = remember {
        Animatable(1f)
    }

    LaunchedEffect(targetFontSize) {
        if (previousFontSize.floatValue != targetFontSize) {
            previousFontSize.floatValue = targetFontSize

            animatedScale.snapTo(1f)
            animatedScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 120,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    return animatedScale.value
}