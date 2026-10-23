package pe.edu.upc.calculator.features.calculator.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.ui.res.stringResource
import pe.edu.upc.calculator.R
import pe.edu.upc.calculator.features.calculator.domain.ExpressionError
import pe.edu.upc.calculator.core.designsystems.theme.CalculatorBackground
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.features.calculator.presentation.components.CalculatorDisplay
import pe.edu.upc.calculator.features.calculator.presentation.components.CalculatorKeypad
import pe.edu.upc.calculator.features.calculator.presentation.components.CalculatorToolbar


@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    state: CalculatorUiState = CalculatorUiState(),
    onDigitClick: (String) -> Unit = {},
    onOperatorClick: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onBackspaceClick: () -> Unit = {},
    onEqualsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onClearHistory: () -> Unit = {},
    onHistoryItemClick: (HistoryItem) -> Unit = {},
    previewResult: (String) -> String? = { null },
    onCursorPositionChange: (Int) -> Unit = {}
) {
    val errorMessage: String? = state.error?.let { error ->
        val resourceId = when (error) {
            ExpressionError.EMPTY_EXPRESSION ->
                R.string.error_empty_expression

            ExpressionError.INCOMPLETE_EXPRESSION ->
                R.string.error_incomplete_expression

            ExpressionError.INVALID_RESULT ->
                R.string.error_invalid_result

            ExpressionError.DIVISION_BY_ZERO ->
                R.string.error_division_by_zero

            ExpressionError.INVALID_PERCENTAGE ->
                R.string.error_invalid_percentage

            ExpressionError.UNCLOSED_PARENTHESIS ->
                R.string.error_unclosed_parenthesis

            ExpressionError.INVALID_DECIMAL ->
                R.string.error_invalid_decimal

            ExpressionError.EXPECTED_NUMBER ->
                R.string.error_expected_number

            ExpressionError.INVALID_NUMBER ->
                R.string.error_invalid_number

            ExpressionError.DIGIT_LIMIT ->
                R.string.error_digit_limit
        }

        stringResource(resourceId)
    }

    var showErrorToast by remember { mutableStateOf(false) }

    LaunchedEffect(state.errorToastId, errorMessage) {
        if (errorMessage == null) {
            showErrorToast = false
        } else {
            showErrorToast = true
            delay(2500.milliseconds)
            showErrorToast = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CalculatorBackground)
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1.1f))

        CalculatorDisplay(
            state = state,
            previewResult = previewResult,
            onCursorPositionChange = onCursorPositionChange
        )

        Spacer(modifier = Modifier.weight(0.9f))

        CalculatorToolbar(
            isHistoryVisible = state.isHistoryVisible,
            onHistoryClick = onHistoryClick,
            onBackspaceClick = onBackspaceClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                CalculatorKeypad(
                    onDigitClick = onDigitClick,
                    onOperatorClick = onOperatorClick,
                    onClearClick = onClearClick,
                    onEqualsClick = onEqualsClick
                )
            }

            AnimatedContent(
                targetState = state.isHistoryVisible,
                modifier = Modifier
                    .matchParentSize()
                    .offset(
                        x = (-16).dp,
                        y = (-16).dp
                    ),
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally(
                            animationSpec = tween(
                                durationMillis = 320,
                                easing = FastOutSlowInEasing
                            ),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 240,
                                delayMillis = 30
                            )
                        ) togetherWith fadeOut(
                            animationSpec = tween(durationMillis = 160)
                        )
                    } else {
                        fadeIn(
                            animationSpec = tween(durationMillis = 1)
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 280,
                                easing = FastOutLinearInEasing
                            ),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeOut(
                            animationSpec = tween(durationMillis = 220)
                        )
                    }
                },
                label = "history_slide"
            ) { isVisible ->
                if (isVisible) {
                    CalculatorHistory(
                        history = state.history,
                        onClearHistory = onClearHistory,
                        onHistoryItemClick = onHistoryItemClick,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-120).dp),
            visible = showErrorToast,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(250))
        ) {
            Row(
                modifier = Modifier
                    .widthIn(max = 340.dp)
                    .background(
                        color = Color(0xCC555555),
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 10.dp
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        }
    }

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun CalculatorScreenPreview() {
    MaterialTheme {
        CalculatorScreen()
    }
}