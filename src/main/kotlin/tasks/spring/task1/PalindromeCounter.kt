package tasks.spring.task1

import input.InputDataReader
import kotlin.math.ceil
import kotlin.math.pow

class PalindromeCounter(inputReader: InputDataReader, enableValidation: Boolean = false) :
    PalindromeCounterBase(inputReader, enableValidation) {

    override fun computeResult(input: Long): Long {
        // Now middle digit has the highest possible value -> algorithm can be launched and no palindromes will be lost,
        // since the substituted value is the largest palindrome which is less or equal to the given number
        val substitutedInput = findFirstPalindromeLessThan(input) ?: return 0

        val numberLength = substitutedInput.length()
        val groundedHalfLength = numberLength / 2
        val hasMiddleDigit = numberLength % 2 == 1
        val substitutedMiddleDigit = getDigitByRank(substitutedInput, groundedHalfLength + 1)
        val middleDigitMultiplier = if (hasMiddleDigit) 10 else 1

        fun howMuchPalindromes(inputHalfWithoutMiddleDigit: Long?, indexFromLeft: Int): Long {
            if (inputHalfWithoutMiddleDigit == null)
                return when {
                    hasMiddleDigit -> (substitutedMiddleDigit + 1).toLong()
                    else -> 1
                }

            // Separate case when a single digit is passed: it is treated like a middle digit, but to skip '0' palindrome we do not add 1 here
            if (groundedHalfLength == 0 && indexFromLeft == 1 && hasMiddleDigit)
                return substitutedMiddleDigit.toLong()

            val highestRankDigit =
                getHighestRankDigit(inputHalfWithoutMiddleDigit)

            val howMuchWithLesserHighestRankDigit =
                if (highestRankDigit > 0) {
                    val variantsCountForCurrentIndex =
                        if (indexFromLeft == 1)
                            highestRankDigit - 1
                        else
                            highestRankDigit

                    variantsCountForCurrentIndex * 10.0.pow(groundedHalfLength - indexFromLeft) * middleDigitMultiplier
                } else {
                    0
                }

            val trimmedHigherRankDigit = removeMostSignificantDigit(inputHalfWithoutMiddleDigit)
            val indexShift = inputHalfWithoutMiddleDigit.length() - (trimmedHigherRankDigit?.length() ?: 1)
            assert(indexShift >= 0) { "Negative shift indicates undesired behaviour: looks like the algorithm feels bad" }

            return howMuchWithLesserHighestRankDigit.toInt() + howMuchPalindromes(
                trimmedHigherRankDigit,
                indexFromLeft + indexShift
            )
        }

        // For some reason TFS does not consider '0' a palindrome (probably the word 'natural palindromes'
        // is missing in the task text), so we subtract 1 at the end of computation
        return countAllPossiblePalindromesWithLengthUpTo(numberLength) + howMuchPalindromes(
            getHalfOfInputIgnoringMiddleDigit(substitutedInput, groundedHalfLength), 1
        )
    }

    private fun countAllPossiblePalindromesWithLengthUpTo(upperLengthBound: Int): Long {
        var palindromesOfShorterLength: Long = 0 //todo sequence + reduce??
        for (shorterLength in 1 until upperLengthBound) {
            palindromesOfShorterLength += countPalindromesOfLength(shorterLength)
        }
        return palindromesOfShorterLength
    }

    private fun getHalfOfInputIgnoringMiddleDigit(input: Long, groundedHalfLength: Int): Long =
        input / 10.0.pow(input.length() - groundedHalfLength).toLong()

    private fun getHighestRankDigit(number: Long): Int = getDigitByRank(number, number.length())

    private fun Long.length() = this.toString().length

    //todo: cache in a map - will be not so much values in it!
    internal fun countPalindromesOfLength(length: Int): Long =
        9 * 10.0.pow(ceil(length.toDouble() / 2.0) - 1).toLong()

    internal fun removeMostSignificantDigit(number: Long): Long? {
        val length = number.length()
        if (length == 1) return null
        return number - 10.0.pow(length - 1).toInt() * getDigitByRank(number, length)
    }
}