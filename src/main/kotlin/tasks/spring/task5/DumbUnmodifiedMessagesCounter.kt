package tasks.spring.task5

import input.InputDataReader

class DumbUnmodifiedMessagesCounter(inputDataReader: InputDataReader): UnmodifiedMessagesCounterBase(inputDataReader) {
    override fun computeResult(input: MessagesQueue): Long {
        val unmodifiedMessages = mutableSetOf<MessageData>()

        for (message in input.messagesRanges) {
            for (intersectedMessage in message.allIntersections(unmodifiedMessages)) {
                unmodifiedMessages.remove(intersectedMessage)
            }
            unmodifiedMessages.add(message)
        }
        return unmodifiedMessages.size.toLong()
    }

    private fun MessageData.allIntersections(messages: Set<MessageData>): List<MessageData> = messages.filter(this::intersectsWith)
}