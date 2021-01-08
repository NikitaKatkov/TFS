package tasks.spring.task1

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class PalindromeCounterTest {
    private fun testSmartWithStatefulReader(testData: String): Long =
        PalindromeCounter(TextInputReader(testData)).solve()

    private fun testDumbWithStatefulReader(testData: String): Long =
        BruteForcePalindromeCounter(TextInputReader(testData)).solve()

    @Test
    fun `test with provided data`() {
        assertEquals(17, testSmartWithStatefulReader("88"))
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
        assertEquals(2345, PalindromeCounter(TextInputReader("")).removeMostSignificantDigit(12345))
        assertEquals(12345, PalindromeCounter(TextInputReader("")).removeMostSignificantDigit(912345))
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
        val lowerBound = 10_000_000L

        val counter = PalindromeCounter(TextInputReader(""))
        // first generate all possible palindromes via brute force
        var bruteCounter = 0L
        val bruteForceTiming = measureNanoTime {
            for (number in lowerBound..10 * lowerBound) {
                if (counter.isPalindrome(number)) {
                    bruteCounter += 1
                }
            }
        }

        val fastCheckTiming = measureNanoTime {
            val fastComputationResult = counter.countPalindromesOfLength(lowerBound.toString().length)
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
    fun `test count all palindromes`() {
        assertEquals(1, testSmartWithStatefulReader("1"))
        assertEquals(5, testSmartWithStatefulReader("5"))
        assertEquals(9, testSmartWithStatefulReader("9"))
        assertEquals(10, testSmartWithStatefulReader("19"))
        assertEquals(17, testSmartWithStatefulReader("88"))
        assertEquals(19, testSmartWithStatefulReader("102"))
        assertEquals(20, testSmartWithStatefulReader("111"))
        assertEquals(109, testSmartWithStatefulReader("1001"))
        assertEquals(1400, testSmartWithStatefulReader("401222"))
        assertEquals(1098, testSmartWithStatefulReader("100000"))
        assertEquals(1099, testSmartWithStatefulReader("100001"))
        assertEquals(1999, testSmartWithStatefulReader("1000001"))
        assertEquals(142947, testSmartWithStatefulReader("${Int.MAX_VALUE.toLong() * 2}"))
    }

    @Test
    fun `test dumb brute force counter`() {
        assertEquals(1, testDumbWithStatefulReader("1"))
        assertEquals(5, testDumbWithStatefulReader("5"))
        assertEquals(9, testDumbWithStatefulReader("9"))
        assertEquals(10, testDumbWithStatefulReader("19"))
        assertEquals(17, testDumbWithStatefulReader("88"))
        assertEquals(19, testDumbWithStatefulReader("102"))
        assertEquals(20, testDumbWithStatefulReader("111"))
        assertEquals(109, testDumbWithStatefulReader("1001"))
        assertEquals(1400, testDumbWithStatefulReader("401222"))
        assertEquals(1098, testDumbWithStatefulReader("100000"))
        assertEquals(1099, testDumbWithStatefulReader("100001"))
        assertEquals(1999, testDumbWithStatefulReader("1000001"))
        // ha-ha, it will take you some time (5.14 minutes on macbook with i9 against 91[!] ms with smart algorithm) to get the answer ;)
        // assertEquals(142947, testDumbWithStatefulReader("${Int.MAX_VALUE.toLong() * 2}"))
    }

    @Test
    fun `test dumb and smart counters equivalent results`() {
        val random = Random(System.currentTimeMillis())
        val randomNumber = "${random.nextInt(1, 1_000_000)}"
        println("Input number: $randomNumber")

        var smartComputationResult: Long
        val smartComputationTime = measureNanoTime {
            smartComputationResult = testSmartWithStatefulReader(randomNumber)
        }
        println("Smart result: $smartComputationResult, computed in $smartComputationTime ns")

        var dumbComputationResult: Long
        val dumbComputationTime = measureNanoTime {
            dumbComputationResult = testDumbWithStatefulReader(randomNumber)
        }
        println("Dumb result: $dumbComputationResult, computed in $dumbComputationTime ns")

        assertEquals(
            dumbComputationResult,
            smartComputationResult,
            "Smart and dumb computation results are not equal!"
        )

        val isSmartFaster = smartComputationTime <= dumbComputationTime
        val gainInTime =
            if (isSmartFaster)
                dumbComputationTime / smartComputationTime
            else
                smartComputationTime / dumbComputationTime

        println("${if (isSmartFaster) "Smart" else "Dumb"} computation happened to be $gainInTime times faster")
    }
}