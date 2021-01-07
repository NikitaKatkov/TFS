package input

object ConsoleInputReader: InputDataReader {
    override fun readOneLine(): String = readLine().orEmpty()
}

class TextInputReader(input: String): InputDataReader {
    private val myState: List<String> = input.split(LINE_SEPARATOR)
    private var myCurrentElementIndex = 0

    override fun readOneLine(): String = myState[myCurrentElementIndex++]

    companion object {
        private const val LINE_SEPARATOR = "\n"
    }
}