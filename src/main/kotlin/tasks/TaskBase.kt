package tasks

import input.InputDataReader

abstract class TaskBase<InputType, ResultType>(
    protected val inputReader: InputDataReader,
    protected val enableValidation: Boolean = false
) {
    abstract fun prepareInput(): InputType
    abstract fun solve(): ResultType
}