package input

import exceptions.ValidationException

object InputValidator {
    fun validateLimits(value: Int, minValue: Int?, maxValue: Int?) {
        validateLimits(value.toLong(), minValue?.toLong(), maxValue?.toLong())
    }

    fun validateLimits(value: Long, minValue: Long?, maxValue: Long?) {
        if (minValue != null && value < minValue) {
            throw ValidationException("$value is less than minimum value $minValue")
        }
        if (maxValue != null && value > maxValue) {
            throw ValidationException("$value is more than maximum value ${maxValue}")
        }
    }
}