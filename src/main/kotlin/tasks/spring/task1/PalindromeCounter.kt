package tasks.spring.task1

import input.InputDataReader
import input.InputParser
import input.InputValidator
import kotlin.math.ceil
import kotlin.math.pow

class PalindromeCounter(private val inputReader: InputDataReader) {
    private fun prepareInputData(): Int = inputReader.readOneLine()
        .let { InputParser.parseInteger(it) }
        .also { InputValidator.validateLimits(it, 1, 100_000) }

    fun solve2(): Int {
        val substitutedInputNumber = findFirstPalindromeLessThan(prepareInputData())!!
        val numberLength = substitutedInputNumber.length()

        var palindromesOfShorterLength = 0
        for (shorterLength in 1 until numberLength) {
            palindromesOfShorterLength += countPalindromesOfLength(shorterLength)
        }

        var result = palindromesOfShorterLength

        // compute middle digit if number length is odd
        val groundedHalfLength = numberLength / 2
        val hasMiddleDigit = numberLength % 2 == 1 && numberLength > 1
        val middleDigitMultiplier =
            if (hasMiddleDigit) getDigitByRank(substitutedInputNumber, groundedHalfLength + 1) + 1 else 1

        fun countPalindromesOfLengthLessThanGiven(restOfGivenNumber: Int): Int {
            if (numberLength == 1 || numberLength - restOfGivenNumber.length() + 1 == groundedHalfLength) { //normalize zero-based index to 1-based length system
                return middleDigitMultiplier * getHighestRankDigit(restOfGivenNumber)
            }

            val highestRankDigit = getHighestRankDigit(restOfGivenNumber)

            // If the first digit is less than HRD, any digits combinations are accepted in the next positions,
            // because the result will be less than the given number anyway.
            // For the only left case when the first digit is the same as the first digit in the given number
            // we must compute recursively other possible palindromes of size (LENGTH - 1)

            val numberWithoutHighestRankDigit =
                removeMostSignificantDigit(restOfGivenNumber, restOfGivenNumber.length())
            val countOfPalindromesOfSameLengthAndLessThanGiven =
                countPalindromesOfLengthLessThanGiven(numberWithoutHighestRankDigit)

            return if (hasMiddleDigit)
                (highestRankDigit - 1) * 10 + countOfPalindromesOfSameLengthAndLessThanGiven
            else
                countOfPalindromesOfSameLengthAndLessThanGiven
        }

        result += countPalindromesOfLengthLessThanGiven(substitutedInputNumber)

        return result
    }

    fun getHighestRankDigit(number: Int): Int = getDigitByRank(number, number.length())

    fun Int.length() = this.toString().length


    fun solve(): Int {
        val input = prepareInputData()

        // to skip various checks in case we've introduced palindrome made of mirrored half-length input number:
        // obviously such scenario doesn't consider smaller ranks of the input number
        // e.g.: for input 9297600 we've found palindrome 9297929 because the algorithm only works with the first half of the given number
        val substitutedInputNumber = findFirstPalindromeLessThan(input)!! //fixme do we need it really?
        val inputAsString = substitutedInputNumber.toString()
        val substitutedInputLength = inputAsString.length
        val hasOddLength =
            substitutedInputLength % 2 == 0 //fixme consider at the end + mb need to compute it against substituted number??
        val groundedHalfLength = substitutedInputLength / 2

        fun palindromesOfLengthWithLimitedValue(maximumValue: Int): Int {
            val currentValueLength = maximumValue.toString().length
            // 1-based??
            val indexOfCurrentHighRankDigitInLeftHalfOfNumber = currentValueLength - groundedHalfLength
            when (indexOfCurrentHighRankDigitInLeftHalfOfNumber) {
                1 -> return maximumValue
                groundedHalfLength -> return getDigitByRank(
                    maximumValue,
                    currentValueLength
                ) * palindromesOfLengthWithLimitedValue(1)//todo!!

            }

            if (isLastIndexBeforeMiddleDigit(currentValueLength, groundedHalfLength, substitutedInputLength)) {
                return maximumValue//fixme consider 0 and self??
            }

            // highest rank digit in a number
            val mostSignificantDigit = getDigitByRank(maximumValue, currentValueLength)

            return mostSignificantDigit * countPalindromesOfLength(currentValueLength - 1) +
                    palindromesOfLengthWithLimitedValue(removeMostSignificantDigit(maximumValue, currentValueLength))
        }

        return palindromesOfLengthWithLimitedValue(substitutedInputNumber)
    }

    private fun isLastIndexBeforeMiddleDigit(
        indexFromEnd: Int,
        groundedHalfLength: Int,
        fullInputLength: Int
    ): Boolean {
        val indexFromLeft = fullInputLength - indexFromEnd + 1 // not a java style zero-based index, but a 1-based
        return indexFromLeft == groundedHalfLength
    }

    // not sure if 0 is considered a palindrome according to the given task text :(
//    private fun countPalindromes(maxValue: Int, currentLength: Int): Int {
//        if (maxValue <= 0) {
//            throw RuntimeException("Computation error: should not reach negative numbers. Value to compute palindrome for is $maxValue, length is $currentLength")
//        }
//        return if (maxValue > 9) {
//            val mostSignificantDigit = getMostSignificantDigit(maxValue, currentLength)
//            (mostSignificantDigit - 1) * countPalindromesOfLength(currentLength - 1) +
//        //countPalindromes(removeMostSignificantDigit(maxValue, currentLength), currentLength - 1)
//        } else {
//            maxValue//fixme 9?
//        }
//    }

    internal fun isPalindrome(number: Int): Boolean {
        val inputLength = number.toString().length
        val groundedHalfLength = inputLength / 2

        for (index in 1..groundedHalfLength) {
            if (getDigitByRank(number, index) != getDigitByRank(number, inputLength - index + 1)) return false
        }
        return true
    }

    internal fun findFirstPalindromeLessThan(value: Int): Int? =
        generateSequence(value) { it - 1 }.firstOrNull { isPalindrome(it) }

    //todo: cache in a map - will be not so much values in it!
    internal fun countPalindromesOfLength(length: Int): Int =
        9 * 10.0.pow(ceil(length.toDouble() / 2.0) - 1).toInt()

    internal fun getDigitByRank(number: Int, rank: Int): Int =
        number / (10.0.pow(rank - 1).toInt()) % 10

    internal fun removeMostSignificantDigit(number: Int, currentLength: Int): Int {
        return number - 10.0.pow(currentLength - 1).toInt() * getDigitByRank(number, currentLength)
    }
}