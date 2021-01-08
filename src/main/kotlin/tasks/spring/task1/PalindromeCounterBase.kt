package tasks.spring.task1

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase
import kotlin.math.pow

abstract class PalindromeCounterBase(inputReader: InputDataReader, enableValidation: Boolean = false): TaskBase<Int, Int>(inputReader, enableValidation) {
    override fun prepareInput(): Int = inputReader.readOneLine()
        .let { InputParser.parseInteger(it) }
        .also { if (enableValidation) InputValidator.validateLimits(it, 1, 100_000) }

    internal fun findFirstPalindromeLessThan(value: Int): Int? =
        generateSequence(value) { it - 1 }.firstOrNull { isPalindrome(it) }

    internal fun isPalindrome(number: Int): Boolean {
        val inputLength = number.toString().length
        val groundedHalfLength = inputLength / 2

        for (index in 1..groundedHalfLength) {
            if (getDigitByRank(number, index) != getDigitByRank(number, inputLength - index + 1)) return false
        }
        return true
    }

    internal fun getDigitByRank(number: Int, rank: Int): Int =
        number / (10.0.pow(rank - 1).toInt()) % 10
}