package kotlin_flow

import coroutines.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

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

/**
 * Asynchronous Flow : Representing Multiple Values - Flows
 * */
class StreamWithFlows {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            launch {
                for (k in 1..3) {
                    println("I am not blocked $k")
                    delay(100L)
                }
            }
            simple().collect { println(it) }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(100L)
                emit(i)
            }
        }
    }
    /*
    * Output
    * I am not blocked 1
    * 1
    * I am not blocked 2
    * 2
    * I am not blocked 3
    * 3
    * */
}

/**
 * Asynchronous Flow : Flows are cold - If you don't call the collect() method on flow the flow won't run.
 * */
class ColdFlowProperty {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            println("Calling simple function")
            val flow = simple()
            println("Calling collect...")
            flow.collect { value -> print(value) }
            println()
            println("Calling collect again...")
            flow.collect { value -> print(value) }
            println()
        }

        private fun simple(): Flow<Int> = flow {
            println("Flow Started")
            for (i in 1..3) {
                delay(1000L)
                emit(i)
            }
        }
    }
    /*
    * Output
    * Calling simple function
    * Calling collect...
    * Flow Started
    * 123 // If you don't call the collect method this line won't print
    * Calling collect again...
    * Flow Started
    * 123 // If you don't call the collect method this line won't print
    * */
}

/**
 * Asynchronous Flow - Flow cancellation basics
 * */
class FlowCancellationBasics {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* A flow gets cancelled when the timeout inside the flow gets expired */
            withTimeoutOrNull(250L) {
                simple().collect { value -> println(value) }
            }
            println("Done")
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(100L)
                println("Emitting $i")
                emit(i)
            }
        }
    }
    /*
    * Output
    * Emitting 1
    * 1
    * Emitting 2
    * 2
    * Done
    * */
}

/**
 * Asynchronous Flow : Flow Builders - asFlow
 * */
class AsFlowIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            (1..3).asFlow().collect { value ->
                delay(1000L)
                print(value)
            }
        }
    }
    /*
    * Output
    * 123
    * */
}

/**
 * Asynchronous Flow : Intermediate Flow Operators - Map Operator
 * */
class MapIntermediateOperator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            (1..3).asFlow().map { request -> performRequest(request) }.collect { response -> println(response) }
        }

        private suspend fun performRequest(request: Int): String {
            delay(1000L)
            return "response $request"
        }
    }
    /*
    * Output
    * response 1
    * response 2
    * response 3
    * */
}

/**
 * Asynchronous Flow - Intermediate Flow Operators - Transform Operator
 * */
class TransformIntermediateOperator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            (1..3).asFlow().transform { request ->
                emit("Making request $request")
                emit(performRequest(request))
            }
            println("Done")
        }

        private suspend fun performRequest(request: Int): String {
            delay(1000L)
            return "response $request"
        }
    }
}

/**
 * Asynchronous Flow - Intermediate Flow Operators - Size Limiting Operators
 * */
class SizeLimitingIntermediateOperator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            numbers().take(2).collect { value -> println(value) }
        }

        private fun numbers(): Flow<Int> = flow {
            try {
                emit(1)
                emit(2)
                println("This line will not execute")
                emit(3)
            } finally {
                print("Finally in numbers method")
            }
        }
    }
    /*
    * Output
    * 1
    * 2
    * Finally in numbers method
    * */
}

/**
 * Asynchronous Flow - Terminal Flow Operators - Reduce
 * */
class ReduceTerminalFlowOperator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val sum = (1..10).asFlow().reduce { accumulator, value -> accumulator + value }
            println(sum)
        }
    }
    /*
    * Output
    * 55
    * */
}

/**
 * Asynchronous Flow : Flows are Sequential - Each emitted value is processed by all the intermediate
 * operators from upstream to downstream and is then delivered to the terminal operator after.
 * */
class SequentialFlowIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            (1..5).asFlow().filter {
                println("Filtering $it")
                it % 2 == 0
            }.map {
                println("Mapping $it")
                "Mapped $it"
            }.collect { value ->
                delay(500L)
                println("Collecting $value")
            }
        }
    }
    /*
    * Output
    * Filtering 1
    * Filtering 2
    * Mapping 2
    * Collecting Mapped 2
    * Filtering 3
    * Filtering 4
    * Mapping 4
    * Collecting Mapped 4
    * Filtering 5
    * */
}

/**
 * Asynchronous Flow - Flow Context - Introduction
 * */
class FlowContextIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().collect { value -> log("Collected $value") }
        }

        private fun simple(): Flow<Int> = flow {
            log("Simple Flow Started")
            for (i in 1..3) {
                delay(1000L)
                emit(i)
            }
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Simple Flow Started
    * Thread : Thread[main,5,main] :: message : Collected 1
    * Thread : Thread[main,5,main] :: message : Collected 2
    * Thread : Thread[main,5,main] :: message : Collected 3
    * */
}

/**
 * Asynchronous Flow - Flow Context - Wrong emission withContext - Throws Error
 * */
class WrongEmissionIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().collect { value -> println(value) }
        }

        private fun simple(): Flow<Int> = flow {
            // The wrong way to change context for CPU-consuming code in flow builder.
            withContext(Dispatchers.Default) {
                for (i in 1..3) {
                    delay(1000L) // Pretend we are computing it in CPU-consuming way.
                    emit(i) // Emit next value
                }
            }
        }
    }
    /*
    * Output
    * Throws Exception
    * */
}

/**
 * Asynchronous Flow - Flow Context - flowOn Operator
 * */
class FlowOnOperatorIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().collect { value -> log("Collected $value") }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(500L)
                log("Emitting $i")
                emit(i)
            }
        }.flowOn(Dispatchers.Default)
    }
    /*
    * Output
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : Emitting 1
    * Thread : Thread[main,5,main] :: message : Collected 1
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : Emitting 2
    * Thread : Thread[main,5,main] :: message : Collected 2
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : Emitting 3
    * Thread : Thread[main,5,main] :: message : Collected 3
    * */
}

/**
 * Asynchronous Flow : Buffering - Without Using Buffer
 * */
class WithoutBufferExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val time = measureTimeMillis {
                simple().collect { value ->
                    delay(500L)
                    println(value)
                }
            }
            println("Completed in $time ms")
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(500L)
                emit(i)
            }
        }
    }
    /*
    * Output
    * 1
    * 2
    * 3
    * Completed in 3177 ms
    * */
}

/**
 * Asynchronous Flow : Buffering - With Buffer Collector
 * */
class WithBufferCollector {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val time = measureTimeMillis {
                simple().buffer().collect { value ->
                    delay(500L)
                    println(value)
                }
            }
            println("Completed in $time ms")
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(500)
                emit(i)
            }
        }
    }
    /*
    * Output
    * 1
    * 2
    * 3
    * Completed in 2161 ms
    * */
}

/**
 * Asynchronous Flow - Buffering - Conflate
 * */
class ConflateExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val time = measureTimeMillis {
                simple().conflate().collect { value ->
                    delay(500L)
                    println(value)
                }
            }
            println("Completed in $time")
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(500L)
                emit(i)
            }
        }
    }
    /*
    * Output
    * 1
    * 2
    * 3
    * Completed in 2155 ms
    * */
}