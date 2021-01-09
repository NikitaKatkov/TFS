package exceptions

import java.lang.Exception

class ComputationException: RuntimeException {
    constructor(cause: Exception) : super(cause)
    constructor(description: String) : super(description)
    constructor(description: String, cause: Exception) : super(description, cause)
}