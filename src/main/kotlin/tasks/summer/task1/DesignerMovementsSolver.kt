package tasks.summer.task1

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase
import kotlin.math.min

internal class DesignerMovementsSolver(inputReader: InputDataReader) : TaskBase<Sequence<Int>, Int>(inputReader, true) {
    // Easy to modify code so that it would work with unlimited sequence of designer movements
    override fun prepareInput(): Sequence<Int> {
        val movementsCount = inputReader.readOneLine()
            .let { InputParser.parseInteger(it) }
            .also { InputValidator.validateLimits(it, 1, 100) }

        return sequence {
            for (index in 0 until movementsCount) {
                yield(
                    inputReader.readOneLine()
                        .let { InputParser.parseInteger(it) }
                        .also { InputValidator.validateLimits(it, -1000, 1000) }
                )
            }
        }
    }

    override fun computeResult(input: Sequence<Int>): Int {
        var currentDistance = DEFAULT_DISTANCE
        var minimumDistance = currentDistance

        input.forEach { currentMoveLength ->
            currentDistance += currentMoveLength
            if (currentDistance < 0) {
                // if designers walked past each other, the distance between them still needs to be positive :)
                currentDistance *= -1
            }
            minimumDistance = min(currentDistance, minimumDistance)
        }
        return minimumDistance
    }

    companion object {
        private const val DEFAULT_DISTANCE = 100
    }
}