package input

import exceptions.ParsingException

object ConsoleInputReader: InputDataReader {
    override fun readOneLine(): String = readLine().orEmpty()
}

class TextInputReader(input: String): InputDataReader {
    private val myState: List<String> = input.split(LINE_SEPARATOR)
    private var myCurrentElementIndex = 0

    override fun readOneLine(): String {
        if (myCurrentElementIndex >= myState.size)
            throw ParsingException("No more lines available in input data! Requested ${myCurrentElementIndex + 1} lines, but only ${myState.size} are present")
        return myState[myCurrentElementIndex++]
    }

    companion object {
        private const val LINE_SEPARATOR = "\n"
    }
}