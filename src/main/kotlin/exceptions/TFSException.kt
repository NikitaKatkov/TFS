package exceptions

abstract class TFSException : RuntimeException {
    constructor(cause: Exception) : super(cause)
    constructor(description: String) : super(description)
    constructor(description: String, cause: Exception) : super(description, cause)
}

class ComputationException : TFSException {
    constructor(cause: Exception) : super(cause)
    constructor(description: String) : super(description)
    constructor(description: String, cause: Exception) : super(description, cause)
}


class ParsingException : TFSException {
    constructor(cause: Exception) : super(cause)
    constructor(description: String) : super(description)
    constructor(description: String, cause: Exception) : super(description, cause)
}

class ValidationException : TFSException {
    constructor(cause: Exception) : super(cause)
    constructor(description: String) : super(description)
    constructor(description: String, cause: Exception) : super(description, cause)
}