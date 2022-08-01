package shared_mutable_state

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * Shared mutable state and concurrency - The problem - It is highly unlikely to ever print
 * "Counter = 100000", because a hundred coroutines increment the counter concurrently from
 * multiple threads without any synchronization.
 * */
class ProblemWithoutSharedMutableState {
    companion object {
        private var counter = 0

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter++
                }
            }
            println("Counter = $counter")
        }

        private suspend fun massiveRun(action: suspend () -> Unit) {
            val n = 100
            val k = 1000
            val time = measureTimeMillis {
                coroutineScope {
                    repeat(n) {
                        launch {
                            repeat(k) {
                                action()
                            }
                        }
                    }
                }
            }
            println("Completed ${n * k} actions in $time ms")
        }
    }
    /*
    * Output
    * Completed 100000 actions in 28 ms
    * Counter = 95108
    * */
}

/**
 * Shared mutable state and concurrency : Volatiles are of no help - This code works slower, but we
 * still don't get "Counter = 100000" at the end, because volatile variables guarantee linearizable
 * (this is a technical term for "atomic") reads and writes to the corresponding variable, but do
 * not provide atomicity of larger actions (increment in our case).
 * */
class UseVolatileInsteadOfSharedMutableState {
    companion object {

        @Volatile
        private var counter = 0 // marked the counter as volatile

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter++
                }
            }
            println("Counter $counter")
        }

        private suspend fun massiveRun(action: suspend () -> Unit) {
            val n = 100
            val k = 1000
            val time = measureTimeMillis {
                coroutineScope {
                    repeat(n) {
                        launch {
                            repeat(k) {
                                action()
                            }
                        }
                    }
                }
            }
            println("Completed ${n * k} actions in $time ms")
        }
    }
    /*
    * Output
    * Completed 100000 actions in 50 ms
    * Counter 95448
    * */
}