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

/**
 * Asynchronous Flow - Buffering - CollectLatest
 * */
class CollectLatestExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val time = measureTimeMillis {
                simple().collectLatest { value ->
                    delay(500)
                    println(value)
                }
            }
            println("Completed in $time")
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
    * Completed in 2169 ms
    * */
}

/**
 * Asynchronous Flow : Composing Multiple Flows - Zip Operator
 * */
class ZipOperatorExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val numbers = (1..3).asFlow()
            val string = flowOf("one", "two", "three")
            numbers.zip(string) { a, b -> "$a -> $b" }.collect { value -> println(value) }
        }
    }
    /*
    * Output
    * 1 -> one
    * 2 -> two
    * 3 -> three
    * */
}

/**
 * Asynchronous Flow : Composing Multiple Forms - Zip vs Combine Operator
 * */
class CompareZipOperator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val numbers = (1..3).asFlow().onEach { delay(300L) }
            val string = flowOf("one", "two", "three").onEach { delay(400L) }
            val startTime = System.currentTimeMillis()
            numbers.zip(string) { a, b -> "$a -> $b" }.collect { value ->
                println("Collected $value in ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }
    /*
    * Output
    * Collected 1 -> one in 453 ms
    * Collected 2 -> two in 858 ms
    * Collected 3 -> three in 1272 ms
    * */
}

/**
 * Asynchronous Flow - Composing Multiple Flows - Combine Operator
 * */
class CombineOperatorExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val numbers = (1..3).asFlow().onEach { delay(300L) }
            val string = flowOf("one", "two", "three").onEach { delay(400L) }
            val startTime = System.currentTimeMillis()
            numbers.combine(string) { a, b -> "$a -> $b" }.collect { value ->
                println("Collected $value in ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }
    /*
    * Output
    * Collected 1 -> one in 487 ms
    * Collected 2 -> one in 691 ms
    * Collected 2 -> two in 893 ms
    * Collected 3 -> two in 1001 ms
    * Collected 3 -> three in 1298 ms
    * */
}

/**
 * Asynchronous Flow - Flattening Flows - flatMapConcat Operator
 * Reference - https://www.youtube.com/watch?v=WV_PYm8A4aQ
 * */
class FlatMapConcatOperatorExample {
    companion object {
        @OptIn(FlowPreview::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            (1..3).asFlow().onEach { delay(100L) }.flatMapConcat { requestFlow(it) }.collect { value ->
                println("Collected $value in ${System.currentTimeMillis() - startTime} ms")
            }
        }

        private fun requestFlow(i: Int): Flow<String> = flow {
            emit("$i First")
            delay(500L)
            emit("$i Second")
        }
    }
    /*
    * Output
    * Collected 1 First in 174 ms
    * Collected 1 Second in 676 ms
    * Collected 2 First in 786 ms
    * Collected 2 Second in 1299 ms
    * Collected 3 First in 1408 ms
    * Collected 3 Second in 1913 ms
    * */
}

/**
 * Asynchronous Flow - Flattening Flows - flatMapMerge Operator
 * Reference - https://www.youtube.com/watch?v=WV_PYm8A4aQ
 * */
class FlatMapMergerOperator {
    companion object {
        @OptIn(FlowPreview::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            (1..3).asFlow().onEach { delay(100L) }.flatMapMerge { requestFlow(it) }.collect { value ->
                println("Collecting $value in ${System.currentTimeMillis() - startTime} ms")
            }
        }

        private fun requestFlow(i: Int): Flow<String> = flow {
            emit("$i First")
            delay(500L)
            emit("$i Second")
        }
    }
    /*
    * Output
    * Collecting 1 First in 201 ms
    * Collecting 2 First in 292 ms
    * Collecting 3 First in 402 ms
    * Collecting 1 Second in 714 ms
    * Collecting 2 Second in 792 ms
    * Collecting 3 Second in 920 ms
    * */
}

/**
 * Asynchronous Flow : Flattening Flows - flatMapLatest
 * Reference - https://www.youtube.com/watch?v=WV_PYm8A4aQ
 * */
class FlatMapLatestExample {
    companion object {
        @OptIn(ExperimentalCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            (1..3).asFlow().onEach { delay(100L) }.flatMapLatest { requestFlow(it) }.collect { value ->
                println("Collecting $value in ${System.currentTimeMillis() - startTime} ms")
            }
        }

        private fun requestFlow(i: Int): Flow<String> = flow {
            emit("$i First")
            delay(500L)
            emit("$i Second")
        }
    }
    /*
    * Output
    * Collecting 1 First in 251 ms
    * Collecting 2 First in 371 ms
    * Collecting 3 First in 489 ms
    * Collecting 3 Second in 1003 ms
    * */
}

/**
 * Asynchronous Flow : Flow Exceptions - Collector try and catch
 * */
class TryCatchInCollectorExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            try {
                simple().collect { value ->
                    println(value)
                    check(value <= 1) {
                        "Collected $value"
                    }
                }
            } catch (e: Throwable) {
                println("Caught $e")
            }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
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
    * Caught java.lang.IllegalStateException: Collected 2
    * */
}

/**
 * Asynchronous Flow : Flow Exceptions - Emitter try and catch - This is better try catch then the try catch on Collector
 * */
class TryCatchInEmitterExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            try {
                simple().collect { value -> println(value) }
            } catch (e: Throwable) {
                println("Caught $e")
            }
        }

        private fun simple(): Flow<String> = flow {
            for (i in 1..3) {
                println("Emitting $i")
                emit(i)
            }
        }.map { value ->
            check(value <= 1) {
                "Crashed on $value"
            }
            "Emitted $value"
        }
    }
    /*
    * Output
    * Emitting 1
    * Emitted 1
    * Emitting 2
    * Caught java.lang.IllegalStateException: Crashed on 2
    * */
}

/**
 * Asynchronous Flow - Exception Transparency - Catch Operator Introduction
 * */
class CatchOperatorIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().catch { e -> emit("Caught $e") }.collect { value ->
                println(value)
            }
        }

        private fun simple(): Flow<String> = flow {
            for (i in 1..3) {
                println("Emitting $i")
                emit(i)
            }
        }.map { value ->
            check(value <= 1) {
                "Crashed on $value"
            }
            "String $value"
        }
    }
    /*
    * Output
    * Emitting 1
    * String 1
    * Emitting 2
    * Caught java.lang.IllegalStateException: Crashed on 2
    * */
}

/**
 * Asynchronous Flow : Exception Transparency - Transparent Catch - Using Check in collect Operator
 * */
class TransparentCatchExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            simple().catch { e -> println("Caught $e") }.collect { value ->
                check(value <= 1) { "Crashed on $value" }
                println(value)
            }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
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
    * Error Message from the system ...
    * */
}

/**
 * Asynchronous Flow : Exception Transparency - Catching Declaratively - Using Check in onEach operator
 * */
class CatchingDeclarativelyExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().onEach { value -> check(value <= 1) { "Crashed on $value" } }.catch { e -> println("Caught $e") }
                .collect { value -> println(value) }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
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
    * Caught java.lang.IllegalStateException: Crashed on 2
    * */
}

/**
 * Asynchronous Flow - Flow Completion - Imperative finally block
 * */
class ImperativeFinallyBlockExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            try {
                simple().collect { value -> println(value) }
            } finally {
                println("Done")
            }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                emit(i)
            }
        }
    }
    /*
    * Output
    * 1
    * 2
    * 3
    * Done
    * */
}

/**
 * Asynchronous Flow - Flow Completion - Declarative Handling - onCompletion Operator
 **/
class DeclarativeHandlingExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().onCompletion { println("Done") }.collect { value -> println(value) }
        }

        private fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                emit(i)
            }
        }
    }
    /*
    * Output
    * 1
    * 2
    * 3
    * Done
    * */
}

/**
 * Asynchronous Flow - Flow Completion - Declarative Handling - onCompletion with catch operator
 * */
class OnCompletionWithCatchExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") else println("Done") }
                .catch { _ -> println("Caught Exception") }.collect { value -> println(value) }
        }

        private fun simple(): Flow<Int> = flow {
            emit(1)
            throw java.lang.RuntimeException()
        }
    }
    /*
    * Output - With Exception
    * 1
    * Flow completed exceptionally
    * Caught Exception
    *
    * Output - Without Exception
    * 1
    * Done
    * */
}

/**
 * Asynchronous Flow : Flow Exception - Successful Completion - Throws Error
 * */
class SuccessfulCompletionExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            simple().onCompletion { cause -> println("Flow completed with $cause") }.collect { value ->
                check(value <= 1) { "Crashed on $value" }
                println(value)
            }
        }

        private fun simple() = (1..3).asFlow()
    }
    /*
    * Output
    * 1
    * Flow completed with java.lang.IllegalStateException: Crashed on 2
    * Error message from system.
    * */
}