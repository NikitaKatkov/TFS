package tasks.spring.task1

import input.InputDataReader

class BruteForcePalindromeCounter(inputReader: InputDataReader, enableValidation: Boolean = false) :
    PalindromeCounterBase(inputReader, enableValidation) {

    override fun computeResult(input: Long): Long {
        val substitution = findFirstPalindromeLessThan(input) ?: return 0

        var palindromeCounter: Long = 0
        for (number in 1..substitution) {
            if (isPalindrome(number)) {
                palindromeCounter += 1
            }
        }
        return palindromeCounter
    }
}