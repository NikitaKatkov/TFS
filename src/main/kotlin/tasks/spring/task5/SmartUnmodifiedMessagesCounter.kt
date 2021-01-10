package tasks.spring.task5

import input.InputDataReader

class SmartUnmodifiedMessagesCounter(inputDataReader: InputDataReader): UnmodifiedMessagesCounterBase(inputDataReader) {
    override fun computeResult(input: MessagesQueue): Long {
        var unmodifiedMessagesCount = 0L
        // First we will sort messages queue by the message begin index - O(N*log(N))
        input.messagesRanges.asSequence()
            .sortedBy { it.beginIndex }
//            .reduce { accumulator, messageData ->
//                // If message ranges intersects, we remember only the latest of them two (by looking at the original order).
//                // If they do not intersect each other, it is guaranteed that no other (yet unprocessed) message would intersect it too (thanks to sorting).
//                //todo: intersects
////                if (accumulator.)
//            }

        return unmodifiedMessagesCount
    }
}