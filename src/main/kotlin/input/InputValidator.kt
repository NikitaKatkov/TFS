package input

import exceptions.ValidationException

object InputValidator {
    fun validateLimits(value: Int, minValue: Int?, maxValue: Int?) {
        if (minValue != null && value < minValue) {
            throw ValidationException("$value is less than minimum value $minValue")
        }
        if (maxValue != null && value > maxValue) {
            throw ValidationException("$value is more than maximum value ${maxValue}")
        }
    }
}