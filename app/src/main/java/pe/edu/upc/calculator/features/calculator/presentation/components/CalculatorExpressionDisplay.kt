package pe.edu.upc.calculator.features.calculator.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import pe.edu.upc.calculator.core.designsystems.theme.CursorColor
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase
import pe.edu.upc.calculator.features.calculator.presentation.utils.CalculatorDisplayLayout
import pe.edu.upc.calculator.features.calculator.presentation.utils.buildCalculatorDisplayText
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorRawCursorPosition
import pe.edu.upc.calculator.features.calculator.presentation.utils.calculatorVisualCursorPosition
import pe.edu.upc.calculator.features.calculator.presentation.utils.createCalculatorDisplayLayout

@Composable
fun CalculatorExpressionDisplay(
    display: String,
    formattedDisplay: String,
    displayText: AnnotatedString,
    displayLayout: CalculatorDisplayLayout,
    cursorPosition: Int,
    formatExpressionUseCase: FormatExpressionUseCase,
    fontSize: Float,
    modifier: Modifier = Modifier,
    onExpressionChange: (String, Int, Int) -> Boolean
) {
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = display,
                selection = TextRange(
                    cursorPosition.coerceIn(0, display.length)
                )
            )
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val textFieldFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        textFieldFocusRequester.requestFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(display, cursorPosition) {
        val safeCursorPosition = cursorPosition.coerceIn(0, display.length)

        when {
            fieldValue.text != display -> {
                fieldValue = TextFieldValue(
                    text = display,
                    selection = TextRange(safeCursorPosition)
                )
            }

            fieldValue.selection.collapsed &&
                    fieldValue.selection.start != safeCursorPosition -> {
                fieldValue = fieldValue.copy(
                    selection = TextRange(safeCursorPosition)
                )
            }
        }
    }

    val expressionTransformation = remember(
        display,
        formattedDisplay,
        displayText,
        displayLayout,
        formatExpressionUseCase
    ) {
        VisualTransformation { text ->
            val expression = text.text
            val formatted = formatExpressionUseCase(expression)

            val layout = if (expression == display) {
                displayLayout
            } else {
                createCalculatorDisplayLayout(formatted)
            }

            val transformedText = if (expression == display) {
                displayText
            } else {
                buildCalculatorDisplayText(
                    visualDisplay = layout.visualDisplay,
                    errorRange = null
                )
            }

            TransformedText(
                text = transformedText,
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        return calculatorVisualCursorPosition(
                            expression = expression,
                            rawPosition = offset,
                            formattedDisplay = formatted,
                            displayLayout = layout,
                            formatExpressionUseCase = formatExpressionUseCase
                        )
                    }

                    override fun transformedToOriginal(offset: Int): Int {
                        return calculatorRawCursorPosition(
                            expression = expression,
                            visualPosition = offset,
                            displayLayout = layout,
                            formatExpressionUseCase = formatExpressionUseCase
                        )
                    }
                }
            )
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        BasicTextField(
            value = fieldValue,
            onValueChange = { newValue ->
                val normalizedText = normalizeCalculatorInput(newValue.text)

                if (normalizedText != null) {
                    val normalizedSelectionStart = normalizeCalculatorInput(
                        newValue.text.take(newValue.selection.start)
                    )?.length

                    val normalizedSelectionEnd = normalizeCalculatorInput(
                        newValue.text.take(newValue.selection.end)
                    )?.length

                    if (
                        normalizedSelectionStart != null &&
                        normalizedSelectionEnd != null
                    ) {
                        val selectionStart = normalizedSelectionStart
                            .coerceIn(0, normalizedText.length)

                        val selectionEnd = normalizedSelectionEnd
                            .coerceIn(0, normalizedText.length)

                        val accepted = onExpressionChange(
                            normalizedText,
                            selectionStart,
                            selectionEnd
                        )

                        if (accepted) {
                            fieldValue = TextFieldValue(
                                text = normalizedText,
                                selection = TextRange(
                                    selectionStart,
                                    selectionEnd
                                )
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(textFieldFocusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        keyboardController?.hide()
                    }
                },
            readOnly = false,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End
            ),
            cursorBrush = SolidColor(CursorColor),
            visualTransformation = expressionTransformation,
            maxLines = Int.MAX_VALUE,
            decorationBox = { innerTextField ->
                innerTextField()
            }
        )
    }
}

private fun normalizeCalculatorInput(input: String): String? {
    val normalized = input
        .filterNot { it.isWhitespace() || it == ',' }
        .replace("-", "−")
        .replace("*", "×")
        .replace("/", "÷")

    return normalized.takeIf {
        it.matches(Regex("""[0-9.+−×÷()%]*"""))
    }
}