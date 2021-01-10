package tasks.spring.task5

import input.InputDataReader
import input.InputParser
import input.InputValidator
import tasks.TaskBase
import kotlin.properties.Delegates

abstract class UnmodifiedMessagesCounterBase(inputDataReader: InputDataReader) :
    TaskBase<MessagesQueue, Long>(inputDataReader) {
    companion object {
        private const val TAPE_LENGTH_LOWER_BOUND = 1L
        private const val TAPE_LENGTH_UPPER_BOUND = 1_000_000_000L
        private const val MESSAGES_COUNT_LOWER_BOUND = 0L
        private const val MESSAGES_COUNT_UPPER_BOUND = 1000L
    }

    public override fun prepareInput(): MessagesQueue {
        val input = MessagesQueue()

        inputReader.run {
            readOneLine()
                .let { InputParser.parseLong(it, input::paperTapeLength) }
                .also { InputValidator.validateLimits(it, TAPE_LENGTH_LOWER_BOUND, TAPE_LENGTH_UPPER_BOUND) }

            readOneLine()
                .let { InputParser.parseLong(it, input::messagesCount) }
                .also { InputValidator.validateLimits(it, MESSAGES_COUNT_LOWER_BOUND, MESSAGES_COUNT_UPPER_BOUND) }

            (1..input.messagesCount).asSequence()
                .map { originalIndex ->
                    readOneLine()
                        .let {
                            val (begin, end) = InputParser.parseTypedPair(it, InputParser::parseLong, " ")
                            InputValidator.validateLimits(begin, TAPE_LENGTH_LOWER_BOUND, TAPE_LENGTH_UPPER_BOUND)
                            InputValidator.validateLimits(end, begin, TAPE_LENGTH_UPPER_BOUND)
                            MessageData(begin, end, originalIndex)
                        }
                }.toList()
                .let { input.messagesRanges = it }
        }

        return input
    }
}

class MessagesQueue {
    // Keeping those 2 variables is not necessary
    var paperTapeLength by Delegates.notNull<Long>()
    var messagesCount by Delegates.notNull<Long>()
    lateinit var messagesRanges: List<MessageData>
}

data class MessageData(
    val beginIndex: Long,
    val endIndex: Long,
    val originalOrder: Long
) {
    // Easier to look whether ranges definitely do not intersect each other and return reversed answer :)
    fun intersectsWith(other: MessageData): Boolean =
        when {
            endIndex < other.beginIndex -> false
            beginIndex > other.endIndex -> false
            else -> true
        }
}