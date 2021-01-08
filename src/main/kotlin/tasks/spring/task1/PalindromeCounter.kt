package tasks.spring.task1

import exceptions.ComputationException
import input.InputDataReader
import input.InputParser
import input.InputValidator
import kotlin.math.ceil
import kotlin.math.pow

class PalindromeCounter(inputReader: InputDataReader, enableValidation: Boolean = false): PalindromeCounterBase(inputReader, enableValidation) {
    override fun prepareInput(): Int = inputReader.readOneLine()
        .let { InputParser.parseInteger(it) }
        .also { if (enableValidation) InputValidator.validateLimits(it, 1, 100_000) }


    //todo use long instead of int?
    override fun solve(): Int {
        // Now middle digit has the highest possible value -> algorithm can be launched and no palindromes will be lost,
        // since the substituted value is the largest palindrome which is less or equal to the given number
        val substitutedInput = findFirstPalindromeLessThan(prepareInput()) ?: return 0

        val numberLength = substitutedInput.length()

        // extract to separate fun
        var palindromesOfShorterLength = 0
        for (shorterLength in 1 until numberLength) {
            palindromesOfShorterLength += countPalindromesOfLength(shorterLength)
        }

        //constants for using in recursive fun
        val groundedHalfLength = numberLength / 2
        val hasMiddleDigit = numberLength % 2 == 1// && numberLength > 1
        val substitutedMiddleDigit = getDigitByRank(substitutedInput, groundedHalfLength + 1)

        fun howMuchPalindromes(inputHalfWithoutMiddleDigit: Int?, indexFromLeft: Int, previousHdr: Int?): Int {
            if (inputHalfWithoutMiddleDigit == null)
                return when {
                    hasMiddleDigit -> substitutedMiddleDigit + 1
                    previousHdr != null -> 1
                    else -> throw ComputationException("number has no middle digit and no prev hrd value is passed - should not reach this code")
                }

            // separate case when a single digit is passed: it is treated like a middle digit, but to skip '0' palindrome we do not add 1 here
            if (groundedHalfLength == 0 && indexFromLeft == 1 && hasMiddleDigit)
                return substitutedMiddleDigit

            // considering 0 and natural numbers from 1 to middle digit value
            if (hasMiddleDigit && indexFromLeft == groundedHalfLength + 1) {// middle digit index + fixme mb inputHalf == null?? todo remove - it is covered by first if else block
                return substitutedMiddleDigit + 1
            }

            val highestRankDigit =
                getHighestRankDigit(inputHalfWithoutMiddleDigit)
            val middleDigitMultiplier =//todo extract to outer fun
                if (hasMiddleDigit) 10 else 1

            val howMuchWithLesserHighestRankDigit =
                if (highestRankDigit > 0) {
                    val variantsCountForCurrentIndex =
                        if (indexFromLeft == 1)
                                highestRankDigit - 1
                        else highestRankDigit

                    variantsCountForCurrentIndex * 10.0.pow(groundedHalfLength - indexFromLeft) * middleDigitMultiplier
                }
                else
                    0

            val trimmedHdr = removeMostSignificantDigit(inputHalfWithoutMiddleDigit, inputHalfWithoutMiddleDigit.length())
            val currentHrd = getHighestRankDigit(inputHalfWithoutMiddleDigit)
            return howMuchWithLesserHighestRankDigit.toInt() + howMuchPalindromes(trimmedHdr, indexFromLeft + 1, currentHrd)
        }

        // For some reason TFS does not consider '0' a palindrome (probably the word 'natural palindromes'
        // is missing in the task text), so we subtract 1 at the end of computation
        return palindromesOfShorterLength + howMuchPalindromes(
            getHalfOfInputIgnoringMiddleDigit(
                substitutedInput,
                groundedHalfLength
            ),
            1,
            null
        )
    }

    private fun getHalfOfInputIgnoringMiddleDigit(input: Int, groundedHalfLength: Int): Int =
        input / 10.0.pow(input.length() - groundedHalfLength).toInt()

    private fun getHighestRankDigit(number: Int): Int = getDigitByRank(number, number.length())

    private fun Int.length() = this.toString().length

    //todo: cache in a map - will be not so much values in it!
    internal fun countPalindromesOfLength(length: Int): Int =
        9 * 10.0.pow(ceil(length.toDouble() / 2.0) - 1).toInt()

    internal fun removeMostSignificantDigit(
        number: Int,
        currentLength: Int
    ): Int? { //todo remove second param and compute it inside fun
        if (number.length() == 1) return null
        return number - 10.0.pow(currentLength - 1).toInt() * getDigitByRank(number, currentLength)
    }
}