package tasks.spring.task2

import input.TextInputReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LetterCollectorTest {
    private fun testWithStatefulReader(testData: String): String =
        LetterCollector(TextInputReader(testData)).solve()

    @Test
    fun `test with alphabet`() {
        val testData = buildString {
            for (char in 'a'..'z') {
                append(char)
            }
        }
        assertEquals("", testWithStatefulReader(testData))
    }

    @Test
    fun `test with a repeated letter`() {
        assertEquals("a", testWithStatefulReader("aaaaaaaaa"))
    }

    @Test
    fun `test with ordinary input`() {
        assertEquals("ab", testWithStatefulReader("aabbbaabbacbbba"))
        assertEquals("a", testWithStatefulReader("abcdea"))
        assertEquals("s", testWithStatefulReader("sos"))
    }
}