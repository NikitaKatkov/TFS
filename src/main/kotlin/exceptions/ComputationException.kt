package exceptions

import java.lang.IllegalArgumentException

class ComputationException(cause: String): IllegalArgumentException(cause)