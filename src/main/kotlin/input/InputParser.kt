package input

import exceptions.ParsingException
import logging.Logger
import kotlin.reflect.KMutableProperty0

object InputParser {
    fun parseInteger(line: String, setter: KMutableProperty0<Int>? = null): Int {
        val parsedValue =
            try {
                line.trim().toInt()
            } catch (exception: NumberFormatException) {
                throw ParsingException("Unable to parse integer from given string: $line", exception)
            }
        setter?.set(parsedValue)
        return parsedValue
    }

    fun parseLong(line: String, setter: KMutableProperty0<Long>? = null): Long {
        val parsedValue =
            try {
                line.trim().toLong()
            } catch (exception: NumberFormatException) {
                throw ParsingException("Unable to parse long from given string: $line", exception)
            }
        setter?.set(parsedValue)
        return parsedValue
    }

    fun parseDouble(line: String, setter: KMutableProperty0<Double>? = null): Double {
        val parsedValue =
            try {
                line.trim().toDouble()
            } catch (exception: NumberFormatException) {
                throw ParsingException("Unable to parse double from given string: $line", exception)
            }
        setter?.set(parsedValue)
        return parsedValue
    }

    fun <Type> parseTypedPair(
        line: String,
        typeParser: (String) -> Type,
        delimiter: String = " ",
        setter: KMutableProperty0<Pair<Type, Type>>? = null
    ): Pair<Type, Type> {
        val parsedValue =
            try {
                val split = line.split(delimiter)
                if (split.size != 2) {
                    Logger.info("Line with a pair of values and a delimiter symbol '$delimiter' contains more than one delimiter")
                    if (split.size < 2) {
                        throw ParsingException("Less than two values can be parsed from given string: $line")
                    }
                }
                typeParser(split[0]) to typeParser(split[1])
            } catch (exception: ParsingException) {
                throw ParsingException("Unable to parse pair of values from given string: $line", exception)
            }
        setter?.set(parsedValue)
        return parsedValue
    }
}