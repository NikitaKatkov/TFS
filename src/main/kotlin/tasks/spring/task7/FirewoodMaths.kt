package tasks.spring.task7

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase

class FirewoodMaths(inputDataReader: InputDataReader) : TaskBase<Pair<Int, Int>, Int>(inputDataReader) {
    override fun prepareInput(): Pair<Int, Int> =
        inputReader.readOneLine()
            .let { InputParser.parseTypedPair(it, InputParser::parseInteger, " ") }
            .also {
                InputValidator.validateLimits(it.first, 1, 100)
                InputValidator.validateLimits(it.second, it.first, 100)
            }

    override fun computeResult(input: Pair<Int, Int>): Int {
        val minimalDepths = mutableSetOf<Int>()

        fun tryAllOperations(expectedResult: Int, depth: Int = 0): Int {
            when {
                expectedResult < input.first -> return -1
                expectedResult == input.first -> {
                    minimalDepths.add(depth)
                    return -1
                }
            }

            for (operation in MathsOperation.values()) {
                if (!operation.inputValidator(expectedResult)) continue

                val processedInput = operation.reverseOperation(expectedResult)
                assert(processedInput != expectedResult) { "Value $expectedResult was not changed after applying reversed operation of $operation" }

                if (processedInput == -1) continue

                tryAllOperations(processedInput, depth + 1)
            }
            return -1
        }

        tryAllOperations(input.second)
        return minimalDepths.minOrNull() ?: -1
    }
}

// Separate operation representation to easily add more of them
enum class MathsOperation(val reverseOperation: (Int) -> Int, val inputValidator: (Int) -> Boolean) {
    MULTIPLY_BY_FOUR({ it / 4 }, { it > 0 && it % 4 == 0 }),
    ADD_THREE({ it - 3 }, { it - 3 > 0 })
}