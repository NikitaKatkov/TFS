package exceptions

import java.lang.Exception
import java.lang.RuntimeException

class ParsingException: RuntimeException {
    constructor(cause: Exception) : super(cause)
    constructor(description: String) : super(description)
    constructor(description: String, cause: Exception) : super(description, cause)
}