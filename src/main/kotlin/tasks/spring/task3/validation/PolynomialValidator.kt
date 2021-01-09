package tasks.spring.task3.validation

import exceptions.ValidationException
import tasks.spring.task3.Polynomial

object PolynomialValidator {
    private const val MAX_FACTOR = 100_000
    private const val MAX_DEGREE = 20

    fun validateFactorsAndDegrees(polynomial: Polynomial) {
        val maximumDegree = polynomial.maximumDegree()
        if (maximumDegree > MAX_DEGREE)
            throw ValidationException("Unsupported degree value detected: $maximumDegree")

        for (degree in 0..maximumDegree) {
            val factorByDegree = polynomial.factorByDegree(degree)
            if (factorByDegree > MAX_FACTOR) {
                throw ValidationException("Unsupported factor value detected: $factorByDegree for degree: $degree")
            }
        }
    }
}