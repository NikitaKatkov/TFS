package tasks.summer.designers

import exceptions.ParsingException
import exceptions.ValidationException
import input.TextInputReader
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DesignerMovementsSolverTest {
    private fun solveWithStatefulReader(taskData: String) = DesignerMovementsSolver(TextInputReader(taskData)).solve()

    @Test
    fun `test parsing exception`() {
        val taskData = """
            2blabla
            5
            -3
        """.trimIndent()
        Assertions.assertThrows(ParsingException::class.java) {
            solveWithStatefulReader(taskData)
        }
    }

    @Test
    fun `test validation exception`() {
        val taskData = """
            2
            5000
            -3
        """.trimIndent()
        Assertions.assertThrows(ValidationException::class.java) {
            solveWithStatefulReader(taskData)
        }
    }

    @Test
    fun `test with provided input`() {
        val taskData = """
            2
            5
            -3
        """.trimIndent()
        assertEquals(100, solveWithStatefulReader(taskData))
    }

    @Test
    fun `test with smaller result`() {
        val taskData = """
            2
            -75
            5
        """.trimIndent()
        assertEquals(25, solveWithStatefulReader(taskData))
    }

    @Test
    fun `test with a large movement`() {
        val taskData = """
            2
            -110
            -15
            5
        """.trimIndent()
        assertEquals(5, solveWithStatefulReader(taskData))
    }
}