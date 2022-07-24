package kotlin_flow

import coroutines.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/**
 * Asynchronous Flow : Representing multiple values - List - You can use List if there is no delay
 * */

class StreamingWithList {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            simple().forEach { value -> print(value) }
        }

        private fun simple(): List<Int> = listOf(1, 2, 3, 4)
    }
    /*
    * Output
    * 1234
    * */
}

/**
 * Asynchronous Flow : Representing multiple values - Sequence - You can use Sequence if there is a delay
 * */
class StreamWithSequence {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            simple().forEach { value -> print(value) }
            println()
        }

        private fun simple(): Sequence<Int> = sequence {
            for (i in 1..5) {
                Thread.sleep(1000)
                yield(i) // returns a value
            }
        }
    }
    /*
    * Output
    * 12345
    * */
}

/**
 * Asynchronous Flow : Representing Multiple Values - Suspending Functions
 * */
class StreamWithSuspendingFunctions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().forEach { value -> print(value) } // Prints the value of list after 1000 ms
        }

        private suspend fun simple(): List<Int> {
            delay(1000L) // halting simple() to return the list
            return listOf(1, 2, 3, 4, 5)
        }
    }
    /*
    * Output
    * 12345
    * */
}
