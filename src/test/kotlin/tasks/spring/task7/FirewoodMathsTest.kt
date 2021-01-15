package tasks.spring.task7

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class FirewoodMathsTest {
    private fun testWithStatefulReader(testData: String): Int =
        FirewoodMaths(TextInputReader(testData)).solve()

    @Test
    fun `test maths`() {
        assertEquals(3, testWithStatefulReader("10 19"))
        assertEquals(-1, testWithStatefulReader("10 20"))
        assertEquals(0, testWithStatefulReader("1 1"))
        assertEquals(1, testWithStatefulReader("1 4"))
        assertEquals(4, testWithStatefulReader("10 49"))
    }
}