package tasks.spring.task3

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PolynomialMultiplierTest {
    private fun testWithStatefulReader(testData: String): Polynomial =
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

}