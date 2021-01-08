package input

import exceptions.ParsingException
import logging.Logger

object InputParser {
    fun parseInteger(line: String, setter: (Int) -> Unit = {}): Int {
        val parsedValue =
            try {
                line.trim().toInt()
            } catch (exception: NumberFormatException) {
                throw ParsingException("Unable to parse integer from given string: $line", exception)
            }
        setter(parsedValue)
        return parsedValue
    }

    fun parseLong(line: String, setter: (Long) -> Unit = {}): Long {
        val parsedValue =
            try {
                line.trim().toLong()
            } catch (exception: NumberFormatException) {
                throw ParsingException("Unable to parse integer from given string: $line", exception)
            }
        setter(parsedValue)
        return parsedValue
    }

    fun parseDouble(line: String, setter: (Double) -> Unit = {}): Double {
        val parsedValue =
            try {
                line.trim().toDouble()
            } catch (exception: NumberFormatException) {
                throw ParsingException("Unable to parse double from given string: $line", exception)
            }
        setter(parsedValue)
        return parsedValue
    }

    fun <Type> parseTypedPair(
        line: String,
        typeParser: (String) -> Type,
        delimiter: String = " ",
        setter: (Pair<Type, Type>) -> Unit = {}
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
        setter(parsedValue)
        return parsedValue
    }
}