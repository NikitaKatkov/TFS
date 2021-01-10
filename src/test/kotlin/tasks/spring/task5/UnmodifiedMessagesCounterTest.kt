package tasks.spring.task5

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class UnmodifiedMessagesCounterTest {
    private fun testWithStatefulReader(testData: String): Long =
        SmartUnmodifiedMessagesCounter(TextInputReader(testData)).solve()

    @Test
    fun `test parsing input`() {
        val testData = """
            10
            3
            8 10
            2 9
            1 3
        """.trimIndent()

        val messagesQueue = SmartUnmodifiedMessagesCounter(TextInputReader(testData)).prepareInput()

        assertEquals(
            MessageData(8, 10, 1),
            messagesQueue.messagesRanges[0]
        )
        assertEquals(
            MessageData(2, 9, 2),
            messagesQueue.messagesRanges[1]
        )
        assertEquals(
            MessageData(1, 3, 3),
            messagesQueue.messagesRanges[2]
        )
    }

    @Test
    fun `test ranges intersection`() {
        assertTrue { MessageData(1, 2, 0).intersectsWith(MessageData(1, 2, 0)) }
        assertTrue { MessageData(1, 2, 0).intersectsWith(MessageData(2, 3, 0)) }
        assertTrue { MessageData(1, 2, 0).intersectsWith(MessageData(0, 1, 0)) }
        assertTrue { MessageData(1, 3, 0).intersectsWith(MessageData(0, 2, 0)) }
        assertTrue { MessageData(1, 3, 0).intersectsWith(MessageData(2, 4, 0)) }
        assertTrue { MessageData(1, 3, 0).intersectsWith(MessageData(0, 5, 0)) }
        assertTrue { MessageData(1, 3, 0).intersectsWith(MessageData(2, 2, 0)) }

        assertFalse { MessageData(10, 30, 0).intersectsWith(MessageData(1, 9, 0)) }
        assertFalse { MessageData(10, 30, 0).intersectsWith(MessageData(40, 50, 0)) }
    }
}