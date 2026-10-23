package pe.edu.upc.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import pe.edu.upc.calculator.features.calculator.domain.ExpressionParser

class ExampleUnitTest {

    @Test
    fun additionIsCorrect() {
        assertEquals(4.0, ExpressionParser("2+2").parse(), 0.000001)
    }

    @Test
    fun implicitMultiplicationWithParenthesesWorks() {
        assertEquals(6.0, ExpressionParser("2(3)").parse(), 0.000001)
    }

    @Test
    fun parenthesesAndOperatorPrecedenceWork() {
        assertEquals(20.0, ExpressionParser("(2+3)*4").parse(), 0.000001)
    }

    @Test
    fun percentageMultiplicationWorks() {
        assertEquals(100.0, ExpressionParser("200*50%").parse(), 0.000001)
    }

    @Test
    fun percentageAdditionWorks() {
        assertEquals(220.0, ExpressionParser("200+10%").parse(), 0.000001)
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
}