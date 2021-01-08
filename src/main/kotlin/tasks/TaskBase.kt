package tasks

import input.InputDataReader

abstract class TaskBase<InputType, ResultType>(
    protected val inputReader: InputDataReader,
    protected val enableValidation: Boolean = false
) {
    protected abstract fun prepareInput(): InputType
    protected abstract fun computeResult(input: InputType): ResultType

    fun solve(): ResultType {
        val input = prepareInput()
        return computeResult(input)
    }
}