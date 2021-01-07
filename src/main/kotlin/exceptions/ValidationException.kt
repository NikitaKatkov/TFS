package exceptions

import java.lang.RuntimeException

class ValidationException(cause: String) : RuntimeException(cause)