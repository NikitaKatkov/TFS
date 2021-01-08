package tasks.spring.task1

import input.TextInputReader
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class PalindromeCounterTest {
    private fun testSmartWithStatefulReaderAndEnabledCache(testData: String): Long =
        PalindromeCounter(TextInputReader(testData), enableValidation = false, enableCache = true).solve()

    private fun testSmartWithStatefulReaderAndDisabledCache(testData: String): Long =
        PalindromeCounter(TextInputReader(testData), enableValidation = false, enableCache = false).solve()

    private fun testDumbWithStatefulReader(testData: String): Long =
        BruteForcePalindromeCounter(TextInputReader(testData), enableValidation = false).solve()

    @Test
    fun `test with provided data`() {
        assertEquals(17, testSmartWithStatefulReaderAndEnabledCache("88"))
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
        assertEquals(1, testSmartWithStatefulReaderAndEnabledCache("1"))
        assertEquals(5, testSmartWithStatefulReaderAndEnabledCache("5"))
        assertEquals(9, testSmartWithStatefulReaderAndEnabledCache("9"))
        assertEquals(10, testSmartWithStatefulReaderAndEnabledCache("19"))
        assertEquals(17, testSmartWithStatefulReaderAndEnabledCache("88"))
        assertEquals(19, testSmartWithStatefulReaderAndEnabledCache("102"))
        assertEquals(20, testSmartWithStatefulReaderAndEnabledCache("111"))
        assertEquals(109, testSmartWithStatefulReaderAndEnabledCache("1001"))
        assertEquals(1400, testSmartWithStatefulReaderAndEnabledCache("401222"))
        assertEquals(1098, testSmartWithStatefulReaderAndEnabledCache("100000"))
        assertEquals(1099, testSmartWithStatefulReaderAndEnabledCache("100001"))
        assertEquals(1999, testSmartWithStatefulReaderAndEnabledCache("1000001"))
        assertEquals(142947, testSmartWithStatefulReaderAndEnabledCache("${Int.MAX_VALUE.toLong() * 2}"))
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
        val lowerNumberBound = 100_000_000L

        val random = Random(System.currentTimeMillis())
        val randomNumber = "${random.nextLong(lowerNumberBound, 2 * lowerNumberBound)}"
        println("Input number: $randomNumber")

        fun computeAndMeasureTime(computationName: String, computation: (String) -> Long): Pair<Long, Long> {
            var result: Long
            val time = measureNanoTime {
                result = computation(randomNumber)
            }
            println(
                """
                >>
                $computationName result: $result
                $time ns [~${time / 10.0.pow(9)} s]
                
            """.trimIndent()
            )

            return result to time
        }

        val (smartNoCacheResult, _) = computeAndMeasureTime("Smart + no cache") {
            testSmartWithStatefulReaderAndDisabledCache(randomNumber)
        }
        val (smartCacheResult, smartCacheTime) = computeAndMeasureTime("Smart + cache") {
            testSmartWithStatefulReaderAndEnabledCache(randomNumber)
        }
        val (dumbResult, dumbTime) = computeAndMeasureTime("Dumb + no cache") {
            testDumbWithStatefulReader(randomNumber)
        }

        assertEquals(dumbResult, smartCacheResult)
        assertEquals(dumbResult, smartNoCacheResult)

        val isSmartFaster = smartCacheTime <= dumbTime
        val gainInTime: Double =
            if (isSmartFaster)
                dumbTime.toDouble() / smartCacheTime.toDouble()
            else
                smartCacheTime.toDouble() / dumbTime.toDouble()

        fun Double.withLimitedFraction(): String {
            val fraction = if (this > 1.0) 0 else 1
            return "%.${fraction}f".format(this)
        }

        println("${if (isSmartFaster) "Smart" else "Dumb"} computation happened to be ${gainInTime.withLimitedFraction()} times faster")
    }
}