package tasks.spring.task2

import input.InputDataReader
import input.InputValidator
import tasks.TaskBase

class LetterCollector(inputDataReader: InputDataReader) : TaskBase<String, String>(inputDataReader) {
    override fun prepareInput(): String =
        inputReader.readOneLine()
            .also { InputValidator.validateLength(it, 2, 1000) }

    override fun computeResult(input: String): String {
        // Using two small sets (max size of used alphabet length). It could be a map with chars and their counts,
        // but it would require to constantly modify its keys.
        // It's not that bad (and even on large inputs it doesn't affect performance) - just a personal preference.
        val allCharSet = mutableSetOf<Char>()
        val repeatedCharSet = mutableSetOf<Char>()

        for (char in input) {
            if (!allCharSet.add(char)) {
                repeatedCharSet.add(char)
            }
        }

        return buildString {
            repeatedCharSet.forEach(this::append)
        }
    }

    private fun MutableMap<Char, Int>.append(key: Char) {
        var existingValue: Int? = this[key]
        if (existingValue == null) {
            this[key] = 0
            existingValue = 0
        }
        this[key] = existingValue + 1
    }
}