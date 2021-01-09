package tasks.spring.task3

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PolynomialMultiplierTest {
    private fun testWithStatefulReader(testData: String): Polynomial =
        PolynomialMultiplier(TextInputReader(testData)).solve()

    @Test
    fun `test parsing simple polynomial`() {
        val testData = "2x^2-x+1"
        val polynomial = Polynomial.fromString(testData)
        assertEquals(2, polynomial.factorByDegree(2))
        assertEquals(-1, polynomial.factorByDegree(1))
        assertEquals(1, polynomial.factorByDegree(0))
        assertEquals(0, polynomial.factorByDegree(4))
        assertEquals(0, polynomial.factorByDegree(-1))
    }
}