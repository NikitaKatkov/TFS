package tasks.spring.task1

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase
import kotlin.math.pow

abstract class PalindromeCounterBase(inputReader: InputDataReader, enableValidation: Boolean = false) :
    TaskBase<Long, Long>(inputReader, enableValidation) {

    override fun prepareInput(): Long = inputReader.readOneLine()
        .let { InputParser.parseLong(it) }
        .also { if (enableValidation) InputValidator.validateLimits(it, 1, 100_000) }

    internal fun findFirstPalindromeLessThan(value: Long): Long? =
        generateSequence(value) { it - 1 }.firstOrNull { isPalindrome(it) }

    internal fun isPalindrome(number: Long): Boolean {
        val inputLength = number.toString().length
        val groundedHalfLength = inputLength / 2

        for (index in 1..groundedHalfLength) {
            if (getDigitByRank(number, index) != getDigitByRank(number, inputLength - index + 1)) return false
        }
        return true
    }

    internal fun getDigitByRank(number: Long, rank: Int): Int {
        val digit = number / (10.0.pow(rank - 1).toLong()) % 10
        val integerDigit = digit.toInt()
        assert(integerDigit.toLong() == digit) { "Loss of precision detected" }
        return integerDigit
    }
}