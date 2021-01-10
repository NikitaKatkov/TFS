package tasks.spring.task5

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class UnmodifiedMessagesCounterTest {
    private fun testWithStatefulReader(testData: String): Long =
        UnmodifiedMessagesCounter(TextInputReader(testData)).solve()

    @Test
    fun `test parsing input`() {
        val testData = """
            10
            3
            8 10
            2 9
            1 3
        """.trimIndent()

        val messagesQueue = UnmodifiedMessagesCounter(TextInputReader(testData)).prepareInput()

        assertEquals(
            MessageData(8, 10, 1),
            messagesQueue.messagesCoordinates[0]
        )
        assertEquals(
            MessageData(2, 9, 2),
            messagesQueue.messagesCoordinates[1]
        )
        assertEquals(
            MessageData(1, 3, 3),
            messagesQueue.messagesCoordinates[2]
        )
    }
}