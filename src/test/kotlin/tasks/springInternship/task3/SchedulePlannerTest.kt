package tasks.springInternship.task3

import tasks.StatefulIO
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SchedulePlannerTest {
    @Test
    fun `test on various inputs`() {
        var testData = """
            7 23 59
            4
            1 0 0
            7 23 00
            1 1 0
            1 1 1
        """.trimIndent()
        assertEquals("1 0 0", SchedulePlanner().solve(StatefulIO(testData)))

        testData = """
            7 22 59
            4
            1 0 0
            7 23 00
            1 1 0
            1 1 1
        """.trimIndent()
        assertEquals("7 23 0", SchedulePlanner().solve(StatefulIO(testData)))

        testData = """
            1 1 0
            4
            1 0 0
            7 23 00
            1 1 0
            1 1 1
        """.trimIndent()
        assertEquals("1 1 0", SchedulePlanner().solve(StatefulIO(testData)))

        testData = """
            1 1 0
            4
            3 0 0
            4 23 59
            5 1 0
            6 1 1
        """.trimIndent()
        assertEquals("3 0 0", SchedulePlanner().solve(StatefulIO(testData)))

        testData = """
            6 12 0
            1
            6 11 30
        """.trimIndent()
        assertEquals("6 11 30", SchedulePlanner().solve(StatefulIO(testData)))
    }
}