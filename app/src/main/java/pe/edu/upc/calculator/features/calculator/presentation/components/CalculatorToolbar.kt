package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorToolbarIconSize
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorToolbarBackspaceIconSize
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorToolbarBackspaceTouchTargetSize
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorToolbarBottomSpacing
import pe.edu.upc.calculator.features.calculator.presentation.utils.BACKSPACE_LONG_PRESS_DELAY_MS
import pe.edu.upc.calculator.features.calculator.presentation.utils.BACKSPACE_REPEAT_INTERVAL_MS
import pe.edu.upc.calculator.core.designsystems.icons.BackspaceFilled
import pe.edu.upc.calculator.core.designsystems.icons.CalculatorFilled
import pe.edu.upc.calculator.core.designsystems.icons.HistoryOutlined
import pe.edu.upc.calculator.core.designsystems.theme.DividerColor
import pe.edu.upc.calculator.core.designsystems.theme.GreenAccent

@Composable
fun CalculatorToolbar(
    isHistoryVisible: Boolean,
    onHistoryClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isBackspacePressed by remember { mutableStateOf(false) }

    LaunchedEffect(isBackspacePressed) {
        if (isBackspacePressed) {
            delay(BACKSPACE_LONG_PRESS_DELAY_MS.milliseconds)

            while (isBackspacePressed) {
                onBackspaceClick()
                delay(BACKSPACE_REPEAT_INTERVAL_MS.milliseconds)
            }
        }
    }

    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.focusProperties {
                        canFocus = false
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (isHistoryVisible) {
                                CalculatorFilled
                            } else {
                                HistoryOutlined
                            }
                        ),
                        contentDescription = null,
                        tint = GreenAccent,
                        modifier = Modifier.size(calculatorToolbarIconSize())
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .focusProperties {
                            canFocus = false
                        }
                        .pointerInput(onBackspaceClick) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false)

                                onBackspaceClick()

                                isBackspacePressed = true

                                try {
                                    do {
                                        val event = awaitPointerEvent()
                                    } while (event.changes.any { it.pressed })
                                } finally {
                                    isBackspacePressed = false
                                }
                            }
                        },
                    shape = CircleShape,
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier.size(calculatorToolbarBackspaceTouchTargetSize()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(BackspaceFilled),
                            contentDescription = "Borrar",
                            tint = GreenAccent,
                            modifier = Modifier.size(calculatorToolbarBackspaceIconSize())
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.size(calculatorToolbarBottomSpacing())
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            thickness = 1.dp,
            color = DividerColor
        )
    }
}