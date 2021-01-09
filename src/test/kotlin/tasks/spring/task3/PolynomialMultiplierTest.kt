package tasks.spring.task3

import exceptions.ParsingException
import input.TextInputReader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class PolynomialMultiplierTest {
    private fun testWithStatefulReader(testData: String): String =
        PolynomialMultiplier(TextInputReader(testData)).solve()

    @Test
    fun `test parsing simple polynomial`() {
        val polynomial = Polynomial.fromString("2x^2 - x + 1")
        assertEquals(2, polynomial.factorByDegree(2))
        assertEquals(-1, polynomial.factorByDegree(1))
        assertEquals(1, polynomial.factorByDegree(0))
        assertEquals(0, polynomial.factorByDegree(4))
        assertEquals(0, polynomial.factorByDegree(-1))
    }

    @Test
    fun `test parsing polynomial with repeated degrees`() {
        val polynomial = Polynomial.fromString("2x^2 + 1x^2 - 7x^2")
        assertEquals(-4, polynomial.factorByDegree(2))
    }

    @Test
    fun `test parsing empty after reduce polynomials`() {
        var polynomial = Polynomial.fromString("-3x^2 + 2x^2 + x^2 + 0x^2")
        assertEquals(0, polynomial.factorByDegree(2))

        polynomial = Polynomial.fromString("0x")
        assertEquals(0, polynomial.factorByDegree(1))
        assertEquals(0, polynomial.factorByDegree(0))

        polynomial = Polynomial.fromString("0")
        assertEquals(0, polynomial.factorByDegree(1))
        assertEquals(0, polynomial.factorByDegree(0))

        polynomial = Polynomial.fromString("2x^0 + 2")
        assertEquals(0, polynomial.factorByDegree(1))
        assertEquals(4, polynomial.factorByDegree(0))
    }

    @Test
    fun `test compute polynomials product`() {
        assertThrows<ParsingException> { testWithStatefulReader("x + 1") }

        var testData = """
            x+1
            1
        """.trimIndent()
        assertEquals("x+1", testWithStatefulReader(testData))

        testData = """
            x+1
            x+1
        """.trimIndent()
        assertEquals("x^2+2x+1", testWithStatefulReader(testData))

        testData = """
            x+1
            0
        """.trimIndent()
        assertEquals("0", testWithStatefulReader(testData))

        testData = """
            x+2
            x^3-x^2+1
        """.trimIndent()
        assertEquals("x^4+x^3-2x^2+x+2", testWithStatefulReader(testData))

        testData = """
            x^4+x^3-2x^2+x+2
            x^17-x^20+1
        """.trimIndent()
        assertEquals("-x^24-x^23+2x^22-x^20-2x^19+x^18+2x^17+x^4+x^3-2x^2+x+2", testWithStatefulReader(testData))

        testData = """
            x^4-3x^12
            -x^4+2x^4-x^20+1-1+17-166
        """.trimIndent()
        assertEquals("3x^32-x^24-3x^16+447x^12+x^8-149x^4", testWithStatefulReader(testData))
    }
}