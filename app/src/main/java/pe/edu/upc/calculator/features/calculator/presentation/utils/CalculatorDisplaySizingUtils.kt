package pe.edu.upc.calculator.features.calculator.presentation.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

fun calculatorDisplayFontSize(
    showingResult: Boolean,
    displayLength: Int
): TextUnit {
    return when {
        showingResult && displayLength > 30 -> 16.sp
        showingResult && displayLength > 26 -> 18.sp
        showingResult && displayLength > 22 -> 22.sp
        showingResult && displayLength > 18 -> 28.sp
        showingResult && displayLength > 15 -> 34.sp
        showingResult && displayLength > 10 -> 52.sp
        showingResult -> 84.sp
        displayLength > 30 -> 16.sp
        displayLength > 24 -> 20.sp
        displayLength > 19 -> 26.sp
        displayLength > 14 -> 34.sp
        displayLength > 8 -> 46.sp
        else -> 76.sp
    }
}