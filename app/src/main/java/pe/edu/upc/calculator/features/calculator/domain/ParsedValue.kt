package pe.edu.upc.calculator.features.calculator.domain

import java.math.BigDecimal

internal data class ParsedValue(
    val value: BigDecimal,
    val isPercent: Boolean = false
)