package tasks.spring.task6

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase
import kotlin.properties.Delegates


class CoordinatesChecker(inputDataReader: InputDataReader) : TaskBase<InputCoordinates, String>(inputDataReader) {
    companion object {
        private const val VERTICES_LOWER_BOUND = 3
        private const val VERTICES_UPPER_BOUND = 100
    }

    override fun prepareInput(): InputCoordinates {
        val input = InputCoordinates()

        inputReader.run {
            readOneLine()
                .let { InputParser.parseInteger(it, input::verticesCount) }
                .also { InputValidator.validateLimits(it, VERTICES_LOWER_BOUND, VERTICES_UPPER_BOUND) }

            (1..input.verticesCount).asSequence()
                .map { InputParser.parseTypedPair(readOneLine(), InputParser::parseDouble) }
                .toList()
                .let { input.verticesCoordinates = it }

            InputParser.parseTypedPair(readOneLine(), InputParser::parseDouble, " ", input::targetCoordinates)
        }

        return input
    }

    override fun computeResult(input: InputCoordinates): String {
        TODO()
    }


    fun computeIntersectionType(
        polygonEdge: LineSegment,
        testVector: LineSegment
    ): LineSegmentsIntersectionType {
        val x1 = polygonEdge.xStartCoordinate
        val x2 = polygonEdge.xEndCoordinate
        val y1 = polygonEdge.yStartCoordinate
        val y2 = polygonEdge.yEndCoordinate

        val x3 = testVector.xStartCoordinate
        val x4 = testVector.xEndCoordinate
        val y3 = testVector.yStartCoordinate
        val y4 = testVector.yEndCoordinate

        val divider = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1)

        val edgeDividend = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)
        val vectorDividend = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)

        if (divider == 0.0) {
            return if (edgeDividend == 0.0 && vectorDividend == 0.0) LineSegmentsIntersectionType.COINCIDE else LineSegmentsIntersectionType.DO_NOT_INTERSECT
        }

//        val vectorCoefficient = vectorDividend / divider
//        val xIntersection = x1 + edgeCoefficient * (x2 - x1)
//        val yIntersection = y1 + vectorCoefficient * (y2 - y1) 

        return when (edgeDividend / divider) {
            0.0, 1.0 -> LineSegmentsIntersectionType.INTERSECT_VERTEX
            in 0.0..1.0 -> LineSegmentsIntersectionType.INTERSECT
            else -> LineSegmentsIntersectionType.DO_NOT_INTERSECT
        }
    }
}

class InputCoordinates {
    var verticesCount by Delegates.notNull<Int>()
    lateinit var verticesCoordinates: List<Pair<Double, Double>>
    lateinit var targetCoordinates: Pair<Double, Double>
}

enum class TargetLocation(val presentableName: String) {
    INSIDE("YES"), OUTSIDE("NO")
}

enum class LineSegmentsIntersectionType {
    COINCIDE, INTERSECT, INTERSECT_VERTEX, DO_NOT_INTERSECT
}

data class LineSegment(
    val xStartCoordinate: Double,
    val yStartCoordinate: Double,
    val xEndCoordinate: Double,
    val yEndCoordinate: Double
)