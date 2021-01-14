package tasks.spring.task6

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CoordinatesCheckerTest {
    private fun testWithStatefulReader(testData: String): String =
        CoordinatesChecker(TextInputReader(testData)).solve()

    @Test
    fun `test intersection search`() {
        val coordinatesChecker = CoordinatesChecker(TextInputReader(""))

        assertEquals(
            LineSegmentsIntersectionType.COINCIDE,
            coordinatesChecker.computeIntersectionType(
                LineSegment(1.0, 1.0, 3.0, 1.0),
                LineSegment(1.0, 1.0, 3.0, 1.0)
            )
        )

        assertEquals(
            LineSegmentsIntersectionType.INTERSECT,
            coordinatesChecker.computeIntersectionType(
                LineSegment(1.0, 1.0, 3.0, 1.0),
                LineSegment(2.0, 0.0, 2.0, 2.0)
            )
        )

        assertEquals(
            LineSegmentsIntersectionType.INTERSECT,
            coordinatesChecker.computeIntersectionType(
                LineSegment(2.0, 0.0, 2.0, 2.0),
                LineSegment(1.0, 1.0, 3.0, 1.0)
            )
        )


        assertEquals(
            LineSegmentsIntersectionType.INTERSECT,
            coordinatesChecker.computeIntersectionType(
                LineSegment(0.5, 0.3, 0.5, 1.3),
                LineSegment(1.0, 0.0, 0.0, 1.0)
            )
        )

        assertEquals(
            LineSegmentsIntersectionType.DO_NOT_INTERSECT,
            coordinatesChecker.computeIntersectionType(
                LineSegment(1.0, 0.0, 0.0, 1.0),
                LineSegment(2.0, 0.0, 0.0, 2.0)
            )
        )

        assertEquals(
            LineSegmentsIntersectionType.INTERSECT_VERTEX,
            coordinatesChecker.computeIntersectionType(
                LineSegment(1.0, 0.0, 0.0, 1.0),
                LineSegment(1.0, 0.0, 0.0, 2.0)
            )
        )

        assertEquals(
            LineSegmentsIntersectionType.INTERSECT,
            coordinatesChecker.computeIntersectionType(
                LineSegment(-1.0, 1.0, 10.0, 1.0),
                LineSegment(0.0, 0.0, 0.5, 0.5)
            )
        )

        assertEquals(
            LineSegmentsIntersectionType.DO_NOT_INTERSECT,
            coordinatesChecker.computeIntersectionType(
                LineSegment(0.0, 0.0, 0.5, 0.5),
                LineSegment(-1.0, 1.0, 10.0, 1.0)
            )
        )
    }
}