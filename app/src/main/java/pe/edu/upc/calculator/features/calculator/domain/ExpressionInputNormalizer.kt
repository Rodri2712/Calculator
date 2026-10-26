package pe.edu.upc.calculator.features.calculator.domain

internal class ExpressionInputNormalizer {

    fun normalize(expression: String): String {
        return expression
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace(" ", "")
    }
}