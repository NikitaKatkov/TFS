package tasks.spring.task1

import input.InputDataReader
import input.InputParser
import input.InputValidator

class BruteForcePalindromeCounter(inputReader: InputDataReader, enableValidation: Boolean = false) :
    PalindromeCounterBase(inputReader, enableValidation) {

    private fun prepareInputData(): Int = inputReader.readOneLine()
        .let { InputParser.parseInteger(it) }
        .also { if (enableValidation) InputValidator.validateLimits(it, 1, 100_000) }

    override fun solve(): Int {
        val substitution = findFirstPalindromeLessThan(prepareInputData()) ?: return 0

        var palindromeCounter = 0
        for (number in 1..substitution) {
            if (isPalindrome(number)) {
                palindromeCounter += 1
            }
        }
        return palindromeCounter
    }
}