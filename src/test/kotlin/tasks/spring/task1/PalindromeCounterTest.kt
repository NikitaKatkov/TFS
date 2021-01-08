package tasks.spring.task1

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class PalindromeCounterTest {
    private fun testWithStatefulReader(testData: String): Int = PalindromeCounter(TextInputReader(testData)).solve2()

    @Test
    fun `test with provided data`() {
        assertEquals(17, testWithStatefulReader("88"))
    }

    @Test
    fun `test get digit by rank`() {
        assertEquals(1, PalindromeCounter(TextInputReader("")).getDigitByRank(987654321, 1))
        assertEquals(5, PalindromeCounter(TextInputReader("")).getDigitByRank(987654321, 5))
        assertEquals(9, PalindromeCounter(TextInputReader("")).getDigitByRank(987654321, 9))
        assertEquals(0, PalindromeCounter(TextInputReader("")).getDigitByRank(987654321, 10))
    }

    @Test
    fun `test remove most significant digit from number`() {
        assertEquals(2345, PalindromeCounter(TextInputReader("")).removeMostSignificantDigit(12345, 5))
        assertEquals(12345, PalindromeCounter(TextInputReader("")).removeMostSignificantDigit(912345, 6))
        assertEquals(912345, PalindromeCounter(TextInputReader("")).removeMostSignificantDigit(912345, 7))
    }

    @Test
    fun `test is palindrome check`() {
        assertTrue { PalindromeCounter(TextInputReader("")).isPalindrome(1) }
        assertTrue { PalindromeCounter(TextInputReader("")).isPalindrome(11) }
        assertTrue { PalindromeCounter(TextInputReader("")).isPalindrome(1112111) }
        assertFalse { PalindromeCounter(TextInputReader("")).isPalindrome(11121112) }
    }

    @Test
    fun `test count palindromes with fixed length`() {
        val counter = PalindromeCounter(TextInputReader(""))
        // first generate all possible palindromes via brute force
        var bruteCounter = 0
        val bruteForceTiming = measureNanoTime {
//            for (number in 1..100) {
            for (number in 10000000..99999999) {
                if (counter.isPalindrome(number)) {
                    bruteCounter += 1
                }
            }
        }

        val fastCheckTiming = measureNanoTime {
            val fastComputationResult = counter.countPalindromesOfLength(8)
            assertEquals(bruteCounter, fastComputationResult)
        }

        // or why should we invent any other ways to solve the task at all? :)
        assertTrue { bruteForceTiming >= fastCheckTiming }
        println("Brute force: $bruteForceTiming ns")
        println("'Fast' check: $fastCheckTiming ns")
        println("'Fast' check happened to be ${bruteForceTiming / fastCheckTiming} times faster")
    }

    @Test
    fun `test find nearest palindrome less than given number`() {
        assertEquals(9296929, PalindromeCounter(TextInputReader("")).findFirstPalindromeLessThan(9297600))
        assertEquals(9991999, PalindromeCounter(TextInputReader("")).findFirstPalindromeLessThan(9992998))
        assertEquals(1111, PalindromeCounter(TextInputReader("")).findFirstPalindromeLessThan(1111))
        assertEquals(1001, PalindromeCounter(TextInputReader("")).findFirstPalindromeLessThan(1110))
    }

    @Test
    fun synthetic() {
        val input = "11011"

        val counter = PalindromeCounter(TextInputReader(input))
        val substitution = counter.findFirstPalindromeLessThan(input.toInt())!!
        println("Substituted to: $substitution")

        var bruteCounter = 0
        for (number in 1..substitution) {
            if (counter.isPalindrome(number)) {
                bruteCounter += 1
//                println(number)
            }
        }
        println("Count via brute force = $bruteCounter") //198 from smaller length + 900 from length 5 on max input of 100_000

        val fastSolution = counter.solve2()
        println("Count via fast check = $fastSolution")

        assertEquals(bruteCounter, fastSolution)
    }

    @Test
    fun `test count all palindromes`() {
        assertEquals(1, testWithStatefulReader("1"))
        assertEquals(5, testWithStatefulReader("5"))
        assertEquals(9, testWithStatefulReader("9"))
        assertEquals(10, testWithStatefulReader("19"))
        assertEquals(17, testWithStatefulReader("88"))
        assertEquals(19, testWithStatefulReader("102"))
        assertEquals(20, testWithStatefulReader("111"))
    }
}