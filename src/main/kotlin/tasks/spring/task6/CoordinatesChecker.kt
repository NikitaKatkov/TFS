package tasks.spring.task6

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase
import kotlin.properties.Delegates
import kotlin.random.Random


class CoordinatesChecker(inputDataReader: InputDataReader) : TaskBase<InputCoordinates, String>(inputDataReader) {
    companion object {
        private const val VERTICES_LOWER_BOUND = 3
        private const val VERTICES_UPPER_BOUND = 100

        private const val ENDLESS_COMPUTATION_GUARD = 1001
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
        if (anyVertexMatches(
                input.verticesCoordinates,
                input.targetCoordinates
            )
        ) return TargetLocation.INSIDE.presentableName

        val edges = edgesOfPolygon(input.verticesCoordinates)

        var computationResult = TargetLocation.UNKNOWN
        var iteration = 0
        while (computationResult == TargetLocation.UNKNOWN && iteration <= ENDLESS_COMPUTATION_GUARD) {
            computationResult = tryCountIntersectionsWithVector(edges, input.targetCoordinates)
            iteration++
        }
        return computationResult.presentableName
    }

    private fun tryCountIntersectionsWithVector(
        edges: List<LineSegment>,
        targetCoordinates: Pair<Double, Double>
    ): TargetLocation {
        val delta = 2.0
        val randomXCoordinate = Random.nextDouble(targetCoordinates.first - delta, targetCoordinates.first + delta)
        val randomYCoordinate = Random.nextDouble(targetCoordinates.second - delta, targetCoordinates.second + delta)

        val explorerVector =
            LineSegment(targetCoordinates.first, targetCoordinates.second, randomXCoordinate, randomYCoordinate)

        val intersectionsCount = edges.count {
            when (computeIntersectionType(it, explorerVector)) {
                LineSegmentsIntersectionType.INTERSECT_VERTEX, LineSegmentsIntersectionType.COINCIDE -> return TargetLocation.UNKNOWN
                LineSegmentsIntersectionType.START_BELONGS_TO_EDGE -> return TargetLocation.INSIDE
                LineSegmentsIntersectionType.INTERSECT -> true
                else -> false
            }
        }

        return if (intersectionsCount % 2 == 0) TargetLocation.OUTSIDE else TargetLocation.INSIDE
    }

    private fun edgesOfPolygon(vertices: List<Pair<Double, Double>>): List<LineSegment> =
        sequence {
            yield(LineSegment(vertices.last(), vertices.first()))
            for (index in 0 until vertices.size - 1) {
                yield(LineSegment(vertices[index], vertices[index + 1]))
            }
        }.toList()

    private fun anyVertexMatches(
        vertices: List<Pair<Double, Double>>,
        targetCoordinates: Pair<Double, Double>
    ): Boolean =
        vertices.any { it == targetCoordinates }

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
        val vectorCoefficient = vectorDividend / divider
        if (vectorCoefficient < 0) return LineSegmentsIntersectionType.INTERSECT_LINE_NOT_VECTOR

        return when (edgeDividend / divider) {
            // assume that start vector coordinates correspond to the given target
            0.0, 1.0 -> LineSegmentsIntersectionType.INTERSECT_VERTEX
            in 0.0..1.0 -> if (vectorCoefficient == 0.0) LineSegmentsIntersectionType.START_BELONGS_TO_EDGE else LineSegmentsIntersectionType.INTERSECT
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
    INSIDE("YES"), OUTSIDE("NO"), UNKNOWN("UNKNOWN")
}

enum class LineSegmentsIntersectionType {
    COINCIDE, INTERSECT, INTERSECT_VERTEX, START_BELONGS_TO_EDGE, INTERSECT_LINE_NOT_VECTOR, DO_NOT_INTERSECT
}

data class LineSegment(
    val xStartCoordinate: Double,
    val yStartCoordinate: Double,
    val xEndCoordinate: Double,
    val yEndCoordinate: Double
) {
    constructor(
        startCoordinates: Pair<Double, Double>,
        endCoordinates: Pair<Double, Double>
    ) : this(startCoordinates.first, startCoordinates.second, endCoordinates.first, endCoordinates.second)
}