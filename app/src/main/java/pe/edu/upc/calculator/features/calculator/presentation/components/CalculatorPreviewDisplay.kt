package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.calculator.core.designsystems.theme.PreviewTextColor
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase
import pe.edu.upc.calculator.features.calculator.presentation.utils.formatCalculatorPreview

@Composable
fun CalculatorPreviewDisplay(
    preview: String?,
    formatExpressionUseCase: FormatExpressionUseCase
) {
    Spacer(modifier = Modifier.height(4.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = preview != null,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 70)
            )
        ) {
            Text(
                text = formatCalculatorPreview(
                    preview = preview,
                    formatExpressionUseCase = formatExpressionUseCase
                ),
                modifier = Modifier.fillMaxWidth(),
                color = PreviewTextColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}