package tasks.spring.task5

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class UnmodifiedMessagesCounterTest {
    private fun testSmartWithStatefulReader(testData: String): Long =
        SmartUnmodifiedMessagesCounter(TextInputReader(testData)).solve()

    private fun testDumbWithStatefulReader(testData: String): Long =
        DumbUnmodifiedMessagesCounter(TextInputReader(testData)).solve()

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
    
    @Test
    fun `test dumb algorithm`() {
        assertEquals(
            1,
            testDumbWithStatefulReader("""
                10
                3
                8 10
                2 9
                1 3
            """.trimIndent())
        )

        assertEquals(
            3,
            testDumbWithStatefulReader("""
                100
                5
                8 10
                2 9
                1 3
                20 30
                50 100
            """.trimIndent())
        )

        assertEquals(
            1,
            testDumbWithStatefulReader("""
                100
                5
                1 3
                2 4
                3 5
                4 6
                5 7
            """.trimIndent())
        )

        assertEquals(
            5,
            testDumbWithStatefulReader("""
                100
                5
                1 2
                3 4
                5 6
                7 8
                9 10
            """.trimIndent())
        )

        assertEquals(
            1,
            testDumbWithStatefulReader("""
                100
                5
                5 7
                4 6
                3 5
                2 4
                1 3
            """.trimIndent())
        )
    }

    @Test
    fun `test smart algorithm`() {
        assertEquals(
            1,
            testSmartWithStatefulReader("""
                10
                3
                8 10
                2 9
                1 3
            """.trimIndent())
        )

//        assertEquals(
//            3,
//            testWithStatefulReader("""
//                100
//                5
//                8 10
//                2 9
//                1 3
//                20 30
//                50 100
//            """.trimIndent())
//        )
//
//        assertEquals(
//            1,
//            testWithStatefulReader("""
//                100
//                5
//                1 3
//                2 4
//                3 5
//                4 6
//                5 7
//            """.trimIndent())
//        )
//
//        assertEquals(
//            5,
//            testWithStatefulReader("""
//                100
//                5
//                1 2
//                3 4
//                5 6
//                7 8
//                9 10
//            """.trimIndent())
//        )
//
//        assertEquals(
//            1,
//            testWithStatefulReader("""
//                100
//                5
//                5 7
//                4 6
//                3 5
//                2 4
//                1 3
//            """.trimIndent())
//        )
    }
}