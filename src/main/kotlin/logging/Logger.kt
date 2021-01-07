package logging

object Logger {

    // No reasons to add extra dependencies like log4j
    // because I'm not sure that it will be possible to compile such code on the server side
    fun info(text: String) {
        println(text)
    }
}