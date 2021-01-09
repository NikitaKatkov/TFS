package tasks.spring.task3

import exceptions.ComputationException
import input.InputDataReader
import tasks.TaskBase
import tasks.spring.task3.validation.PolynomialValidator

class PolynomialMultiplier(inputDataReader: InputDataReader) : TaskBase<List<Polynomial>, Polynomial>(inputDataReader) {
    override fun prepareInput(): List<Polynomial> {
        // consider reading an arbitrary number of polynomials to multiply in case there are mre than two of them
        val givenPolynomialsCount = 2

        val parsedPolynomials = mutableListOf<Polynomial>()
        for (index in 0 until givenPolynomialsCount) {
            val polynomial = Polynomial.fromString(inputReader.readOneLine())
            PolynomialValidator.validateFactorsAndDegrees(polynomial)
            parsedPolynomials.add(index, polynomial)
        }
        return parsedPolynomials
    }

    override fun computeResult(input: List<Polynomial>): Polynomial =
        input.reduce { accumulator, newPolynomial ->
            multiply(accumulator, newPolynomial)
        }

    private fun multiply(first: Polynomial, second: Polynomial): Polynomial {
        TODO()
    }
}

/**
 * A number with index 'i' in the collection corresponds to coefficient which is a multiplier for variable 'x' in 'n_i * x^i'.
 * Simpler explanation: collection is made of numerical factors for corresponding variable degrees in the original polynomial.
 */
class Polynomial {
    constructor(map: MutableMap<Int, Int>) {
        factorsByDegrees = map
    }

    constructor() {
        factorsByDegrees = mutableMapOf()
    }

    private val factorsByDegrees: MutableMap<Int, Int>

    fun factorByDegree(degree: Int): Int = factorsByDegrees[degree] ?: 0
    fun maximumDegree(): Int = factorsByDegrees.keys.maxOrNull() ?: 0

    fun addMonomialFromString(monomial: String) {
        val (degree, factor) = parseMonomialDegreeToFactor(monomial)
        factorsByDegrees.append(degree, factor)
    }

    companion object {
        private const val VARIABLE_SYMBOL = "x"
        private const val FACTOR_GROUP_INDEX = 1
        private const val VARIABLE_GROUP_INDEX = 2
        private const val DEGREE_GROUP_INDEX = 4

        private val knownArithmeticOperations = setOf("+", "-")

        private val whitespaceRegex = "\\s".toRegex()
        private val monomialRegex: Regex

        init {
            val signs = buildString { knownArithmeticOperations.forEach(this::append) }
            monomialRegex = "([$signs]?\\d*)($VARIABLE_SYMBOL(\\^([$signs]?\\d+))?)?".toRegex()
        }

        fun fromString(rawInput: String): Polynomial {
            val inputWithoutWhitespaces = rawInput.replace(whitespaceRegex, "")

            val polynomial = Polynomial()
            var currentIndex = 0
            while (currentIndex < inputWithoutWhitespaces.length) {
                val foundIndex = inputWithoutWhitespaces.findNextArithmeticSignIndex(currentIndex + 1) ?: inputWithoutWhitespaces.length
                val monomialSubstring = inputWithoutWhitespaces.substring(currentIndex, foundIndex)
                polynomial.addMonomialFromString(monomialSubstring)

                currentIndex = foundIndex
            }
            return polynomial
        }

        private fun String.findNextArithmeticSignIndex(startIndex: Int): Int? =
            knownArithmeticOperations.asSequence()
                .mapNotNull {
                    val indexOfSign = indexOf(it, startIndex)
                    indexOfSign.takeIf { it != -1 }
                }
                .minOrNull()

        private fun parseMonomialDegreeToFactor(rawInput: String): Pair<Int, Int> {
            val groupValues = monomialRegex.matchEntire(rawInput)?.groupValues
                ?: throw ComputationException("Invalid input! Monomial from input part does not match the default pattern: $rawInput")
            val degree: Int
            val factor: Int
            try {
                val isVariablePresent = groupValues[VARIABLE_GROUP_INDEX].isNotBlank()
                degree =
                    if (isVariablePresent)
                        groupValues[DEGREE_GROUP_INDEX].takeIf { it.isNotBlank() }?.toInt() ?: 1
                    else
                        0

                factor = groupValues[FACTOR_GROUP_INDEX]
                    .takeIf { it.isNotBlank() }
                    .let {
                        if (knownArithmeticOperations.contains(it))
                            "${it}1"
                        else
                            it
                    }?.toInt() ?: 1
            } catch (exception: NumberFormatException) {
                throw ComputationException("Unable to parse factor or degree for given monomial: $rawInput", exception)
            }
            return degree to factor
        }
    }
}


fun MutableMap<Int, Int>.append(key: Int, value: Int) {
    var existingValue: Int? = this[key]
    if (existingValue == null) {
        this[key] = 0
        existingValue = 0
    }
    this[key] = existingValue + value
}