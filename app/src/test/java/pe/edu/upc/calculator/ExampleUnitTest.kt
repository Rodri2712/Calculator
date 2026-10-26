package pe.edu.upc.calculator

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import pe.edu.upc.calculator.features.calculator.domain.ExpressionParser
import pe.edu.upc.calculator.features.calculator.presentation.CalculatorDigitLimitValidator

class ExampleUnitTest {

    @Test
    fun additionIsCorrect() {
        assertDecimalEquals("4", "2+2")
    }

    @Test
    fun implicitMultiplicationWithParenthesesWorks() {
        assertDecimalEquals("6", "2(3)")
    }

    @Test
    fun parenthesesAndOperatorPrecedenceWork() {
        assertDecimalEquals("20", "(2+3)*4")
    }

    @Test
    fun percentageMultiplicationWorks() {
        assertDecimalEquals("100", "200*50%")
    }

    @Test
    fun percentageAdditionWorks() {
        assertDecimalEquals("220", "200+10%")
    }

    @Test
    fun decimalAdditionHasNoFloatingPointError() {
        assertDecimalEquals("0.3", "0.1+0.2")
    }

    @Test
    fun divisionByZeroThrowsException() {
        assertThrows(IllegalArgumentException::class.java) {
            ExpressionParser("1/0").parse()
        }
    }

    @Test
    fun missingClosingParenthesisThrowsException() {
        assertThrows(IllegalArgumentException::class.java) {
            ExpressionParser("(2+3").parse()
        }
    }

    @Test
    fun repeatedPercentageThrowsException() {
        assertThrows(IllegalArgumentException::class.java) {
            ExpressionParser("50%%").parse()
        }
    }

    @Test
    fun numberWith15DigitsDoesNotExceedLimit() {
        val display = "123456789012345"
        val validator = CalculatorDigitLimitValidator()

        assertEquals(
            false,
            validator.exceedsLimit(display, display.length)
        )
    }

    @Test
    fun numberWith16DigitsExceedsLimit() {
        val display = "1234567890123456"
        val validator = CalculatorDigitLimitValidator()

        assertEquals(
            true,
            validator.exceedsLimit(display, display.length)
        )
    }

    @Test
    fun digitsInSeparateNumbersAreCountedIndependently() {
        val display = "123456789012345+67890"
        val validator = CalculatorDigitLimitValidator()

        assertEquals(
            false,
            validator.exceedsLimit(display, 15)
        )
    }

    @Test
    fun decimalSeparatorDoesNotCountAsDigit() {
        val display = "12345678901234.5"
        val validator = CalculatorDigitLimitValidator()

        assertEquals(
            false,
            validator.exceedsLimit(display, display.length)
        )
    }

    private fun assertDecimalEquals(expected: String, expression: String) {
        val actual = ExpressionParser(expression).parse()

        assertEquals(
            "Resultado incorrecto para: $expression",
            0,
            BigDecimal(expected).compareTo(actual)
        )
    }
}