package pe.edu.upc.calculator.features.calculator.presentation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import pe.edu.upc.calculator.core.designsystems.theme.EqualsButtonColor
import pe.edu.upc.calculator.core.designsystems.theme.NumberButtonColor
import pe.edu.upc.calculator.core.designsystems.theme.OperatorButtonColor
import pe.edu.upc.calculator.features.calculator.presentation.components.ButtonStyle

fun calculatorButtonBackgroundColor(style: ButtonStyle): Color {
    return when (style) {
        ButtonStyle.Number -> NumberButtonColor
        ButtonStyle.Operator -> OperatorButtonColor
        ButtonStyle.Equals -> EqualsButtonColor
    }
}

fun calculatorButtonTextColor(style: ButtonStyle): Color {
    return when (style) {
        ButtonStyle.Number -> Color.White
        ButtonStyle.Operator -> Color.Black
        ButtonStyle.Equals -> Color.White
    }
}

fun calculatorButtonTextSize(label: String): TextUnit {
    return when (label) {
        "( )", "+/−" -> 22.sp
        "÷", "×", "−", "+" -> 34.sp
        "=" -> 36.sp
        else -> 32.sp
    }
}

fun calculatorButtonFontWeight(style: ButtonStyle): FontWeight {
    return when (style) {
        ButtonStyle.Operator -> FontWeight.Medium
        else -> FontWeight.Normal
    }
}