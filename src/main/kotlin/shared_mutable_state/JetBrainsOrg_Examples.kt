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