package tasks.spring.task5

import input.InputDataReader

class SmartUnmodifiedMessagesCounter(inputDataReader: InputDataReader) :
    UnmodifiedMessagesCounterBase(inputDataReader) {

    override fun computeResult(input: MessagesQueue): Long {
        var unmodifiedMessagesCount = 0L
        // First we will sort messages queue by the message begin index - O(N*log(N)).
        // Next we look through all elements again - O(N) - and count all gaps between neighbours. Obviously a number of gaps
        // plus one (if the messages queue was not empty) is a number of messages that survived the whole chain of tape modifications.

        // Complexity is O(N*log(N) + N) ~ O(N*log(N)) - which is better than the dumb comparison of all the new messages against all the 'written' ones
        input.messagesRanges.asSequence()
            .sortedBy { it.beginIndex }
            .reduce { previousMessage, currentMessage ->
                // If message ranges intersects, we remember only the latest of them two (by looking at the original order).
                // If they do not intersect each other, it is guaranteed that no other (yet unprocessed) message would intersect it too (thanks to sorting).


                // todo: rewrite)) introduce unionRanges(...) fun, return united range if they intersect and latest range if not
                if (previousMessage.intersectsWith(currentMessage)) {
                    getLatestMessage(previousMessage, currentMessage)
                } else {
                    unmodifiedMessagesCount += 1
                    currentMessage
                }
            }

        return unmodifiedMessagesCount.let {
            if (input.messagesCount > 0) it + 1 else it
        }
    }
}