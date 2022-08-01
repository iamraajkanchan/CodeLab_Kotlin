package shared_mutable_state

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
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

/**
 * Shared mutable state and concurrency - Thread Safe Data Structures - AtomicInteger - It works for
 * plain counters, collections, queues and other standard data structures and basic operations on them.
 * However, it does not easily scale to complex state or to complex operations that do not
 * have ready-to-use thread-safe implementations.
 * */
class AtomicIntegerExample {
    companion object {

        private var counter = AtomicInteger()

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter.incrementAndGet()
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
    * Completed 100000 actions in 42 ms
    * Counter 100000
    * */
}

/**
 * Shared mutable state and concurrency - Thread confinement fine-grained
 * */
class SingleThreadContextForFineGrained {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        private var counterContext = newSingleThreadContext("CounterContext")
        private var counter = 0

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            withContext(Dispatchers.Default) {
                withContext(counterContext) {
                    massiveRun {
                        counter++
                    }
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
    * Completed 100000 actions in 57 ms
    * Counter 100000
    * */
}

/**
 * Shared mutable state and concurrency - Thread confinement coarse-grained
 * */
class SingleThreadContextForCoarseGrained {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        private val counterContext = newSingleThreadContext("CounterContext")
        private var counter = 0

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            withContext(counterContext) {
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
    * Completed 100000 actions in 27 ms
    * Counter 100000
    * */
}

/**
 * Shared mutable state and concurrency - Mutual Exclusion - Mutex
 * */
class MutexWithConcurrency {
    companion object {
        private val mutex = Mutex()
        private var counter = 0

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    mutex.withLock {
                        counter++
                    }
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
    * Completed 100000 actions in 912 ms
    * Counter 100000
    * */
}

/**
 * Shared mutable state anc concurrency - Actors
 * */

sealed class CounterMsg
object IncCounter : CounterMsg() // One-way message to increment counter.
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() //
class ActorsWithConcurrency {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val counter = counterActor()
            withContext(Dispatchers.Default) {
                massiveRun {
                    counter.send(IncCounter)
                }
            }
            val response = CompletableDeferred<Int>()
            counter.send(GetCounter(response))
            println("Counter ${response.await()}")
            counter.close()
        }

        @OptIn(ObsoleteCoroutinesApi::class)
        private fun CoroutineScope.counterActor() = actor<CounterMsg> {
            var counter = 0
            for (msg in channel) {
                when (msg) {
                    is IncCounter -> counter++
                    is GetCounter -> msg.response.complete(counter)
                }
            }
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
    * Completed 100000 actions in 1235 ms
    * Counter 100000
    * */
}