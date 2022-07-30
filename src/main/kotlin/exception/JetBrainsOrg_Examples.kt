package exception

import kotlinx.coroutines.*

/**
 * Coroutine Exceptions Handling - Exception Propagation
 * */
class ExceptionPropagationExample {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val job = GlobalScope.launch {
                println("Throwing Exception from launch")
                throw IndexOutOfBoundsException()
            }
            job.join()
            println("Joined failed job")
            val deferred = GlobalScope.async {
                println("Throwing Exception from async")
                throw ArithmeticException()
            }
            try {
                deferred.await() // Waiting for the Async task to complete
                println("Unreached")
            } catch (e: ArithmeticException) {
                println("Caught ArithmeticException")
            }
        }
    }
    /*
    * Output
    * Throwing Exception from launch
    * Exception message from the system...
    * Joined failed job
    * Throwing Exception from async
    * Caught ArithmeticException
    * */
}