package pe.edu.upc.calculator.features.calculator.presentation.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberCalculatorDisplayFontSize(
    targetFontSize: Float
): Float {
    val animatedFontSize = remember {
        Animatable(targetFontSize)
    }

    LaunchedEffect(targetFontSize) {
        val isShrinking = targetFontSize < animatedFontSize.value

        animatedFontSize.animateTo(
            targetValue = targetFontSize,
            animationSpec = tween(
                durationMillis = if (isShrinking) 180 else 260,
                easing = FastOutSlowInEasing
            )
        )
    }

    return animatedFontSize.value
}

@Composable
fun rememberCalculatorCursorAlpha(): Float {
    val cursorTransition = rememberInfiniteTransition(
        label = "calculator_cursor"
    )

    val cursorAlpha by cursorTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    return cursorAlpha
}

class CalculatorHistoryAnimation(
    val scale: Animatable<Float, AnimationVector1D>,
    val offsetX: Animatable<Float, AnimationVector1D>
)

@Composable
fun rememberCalculatorHistoryAnimation(
    historySelectionId: Int
): CalculatorHistoryAnimation {
    val historyScale = remember(historySelectionId) {
        Animatable(
            if (historySelectionId > 0) 0.88f else 1f
        )
    }

    val historyOffsetX = remember(historySelectionId) {
        Animatable(0f)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(historySelectionId) {
        if (historySelectionId > 0) {
            scope.launch {
                historyScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 420,
                        easing = LinearOutSlowInEasing
                    )
                )
            }

            historyOffsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 420,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    }

    return remember(historyScale, historyOffsetX) {
        CalculatorHistoryAnimation(
            scale = historyScale,
            offsetX = historyOffsetX
        )
    }
}