package coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun log(message: String) = println("Thread : ${Thread.currentThread()} :: message : $message")

/**
 * Coroutines Basics | Your First Coroutine
 * */
class SimpleCoroutine {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            launch {
                delay(1000L)
                println("World!!!")
            }
            println("Hello")
        }
    }
}

/**
 * Coroutines Basics | Extract Function Refactoring
 * */
class SimpleCoroutineWithRefactoring {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            println("Name of the thread in runBlocking: ${Thread.currentThread()}")
            launch {
                println("Name of the thread in launch: ${Thread.currentThread()}")
                /* You cannot call doWork() if it is a non-static method */
                /* */
                doWork()
            }
            println("Hello")
        }

        private suspend fun doWork() {
            delay(1000L)
            println("World!!!")
        }
    }
}

/**
 * Coroutines Basics | Scope Builder
 * */
class SimpleCoroutineWithScopeBuilder {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* doWorld() is called inside a child coroutine, so it is suspended before completing */
            launch {
                doWorld()
            }
            log("Hello")
        }

        private suspend fun doWorld() = coroutineScope {
            delay(1000L)
            log("World!!!")
        }

        /*
        * Output
        * Thread : Thread[main,5,main] :: message : Hello
        * Thread : Thread[main,5,main] :: message : World!!!
        * */
    }
}

/**
 * Coroutines Basics | Scope Builder
 * */
class SimpleCoroutineDefiningAdvantageOfChildCoroutine {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* doWork() is called outside a child coroutine, so it is completed first */
            doWork()
            /* If you call doWork() inside a child coroutine, then it is suspended before completing */
            log("Hello")
        }

        private suspend fun doWork() {
            delay(1000L)
            log("World!!!")
        }
        /*
        * Thread : Thread[main,5,main] :: message : World!!!
        * Thread : Thread[main,5,main] :: message : Hello
        * */
    }
}

/**
 * Coroutines Basics | Scope Builder and Concurrency
 * */
class ConcurrentCoroutineWithScopeBuilder {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* doWork() is called outside a child coroutine, so it is completed first. */
            doWork()
            /* If you call doWork() inside a child coroutine, then it is suspended before completing */
            log("Done")
        }

        private suspend fun doWork() = coroutineScope {
            launch {
                delay(1000L)
                log("World 1")
            }
            launch {
                delay(2000L)
                log("World 2")
            }
            log("Hello")
        }
    }
}

/**
 * Coroutines Basics | An explicit Job
 * */
class SimpleCoroutineWitJob {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* launch is a child coroutine of runBlocking */
            val job = launch {
                doWork()
            }
            delay(1200L) // Completed First
            job.join() // waits for the child coroutine to complete
            log("World!!!") // Completed Third
        }

        private suspend fun doWork() {
            delay(1000L)
            log("Hello") // Completed Second
        }
    }

    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Hello
    * Thread : Thread[main,5,main] :: message : World!!!
    *
    * */
}

/**
 * Cancellation and Timeouts : Cancelling Coroutine Execution
 * */
class SimpleCoroutineWithCancelAndJoinMethods {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    log("job: I am sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L)
            log("main: I am tired of waiting")
            job.cancel()
            job.join()
            /* join() is used to complete the termination of a coroutine */
            log("main: Now I can quit")
        }
        /*
        * Output
        * Thread : Thread[main,5,main] :: message : job: I am sleeping 0 ...
        * Thread : Thread[main,5,main] :: message : job: I am sleeping 1 ...
        * Thread : Thread[main,5,main] :: message : job: I am sleeping 2 ...
        * Thread : Thread[main,5,main] :: message : main: I am tired of waiting
        * Thread : Thread[main,5,main] :: message : main: Now I can quit
        * */
    }
}

/**
 * Cancellation and Timeouts : Cancellation is Cooperative
 * */
class SimpleCoroutineWithCancelAndJoinMethod {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    log("job: I am sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L)
            log("main: I am tired of waiting")
            job.cancelAndJoin()
            log("main: Now I can quit")
        }
        /*
        * Output
        * Thread : Thread[main,5,main] :: message : job: I am sleeping 0 ...
        * Thread : Thread[main,5,main] :: message : job: I am sleeping 1 ...
        * Thread : Thread[main,5,main] :: message : job: I am sleeping 2 ...
        * Thread : Thread[main,5,main] :: message : main: I am tired of waiting
        * Thread : Thread[main,5,main] :: message : main: Now I can quit
        * */
    }
}

/**
 * Cancellation and Timeouts : Making Computation code Cancellable - Failed Attempt with Cancel And Join Method
 * */
class SimpleCoroutineFailedCancelAttemptWithCancelJoin {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("job: I am sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            log("main: I am tired of waiting.")
            job.cancel()
            job.join()
            /* cancel() and join() failed to stop the child coroutine */
            log("main: Now I ready to quit!!!")
        }
    }

    /*
    * Output
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 0 ...
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 1 ...
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 2 ...
    Thread : Thread[main,5,main] :: message : main: I am tired of waiting.
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 3 ...
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 4 ...
    Thread : Thread[main,5,main] :: message : main: Now I ready to quit!!!
    */
}

/**
 * Cancellation and Timeouts : Making Computation code Cancellable - Failed Attempt with CancelAndJoin Method
 * */
class SimpleCoroutineFailedCancelAttemptWithCancelAndJoin {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() > nextPrintTime) {
                        log("job: I am sleeping ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            log("main: I am tired of waiting")
            job.cancelAndJoin()
            /* Even the cancelAndJoin() failed to stop the child coroutine */
            log("main: Now I can quit")
        }
    }
    /*
    * Output
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 0
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 1
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 2
    Thread : Thread[main,5,main] :: message : main: I am tired of waiting
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 3
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 4
    Thread : Thread[main,5,main] :: message : main: Now I can quit
    *
    */
}

/**
 * Cancellation and Timeouts : Making Computation code Cancellable with isActive
 * */
class SimpleCoroutineCancelWithIsActive {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextTime = startTime
                var i = 0
                /* HardCoded limit is replaced with the active state of Coroutine. */
                while (isActive) {
                    if (System.currentTimeMillis() > nextTime) {
                        log("job: I am sleeping ${i++}")
                        nextTime += 500L
                    }
                }
            }
            delay(1300L)
            log("main: I am tired of waiting")
            job.cancelAndJoin()
            log("main: Now I can quit")
        }
    }
    /*
    *
    * Output
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 0
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 1
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 2
    Thread : Thread[main,5,main] :: message : main: I am tired of waiting
    Thread : Thread[main,5,main] :: message : main: Now I can quit
    *
    *  */
}

/**
 * Cancellation and Timeouts : Making Computation code Cancellable with isActive and cancel()
 * */
class SimpleCoroutineCancelWithIsActiveAndCancel {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            /* If Main is the context of launch coroutine, then you can't cancel the job */
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) {
                    if (System.currentTimeMillis() > nextPrintTime) {
                        log("job: I am sleeping ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            log("main: I am tired of waiting")
            /* If you are using isActive to cancel a coroutine then you might not need join()  */
            job.cancel()
            log("main: Now I can quit!!!")
        }
    }

    /*
    * Output
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 0
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 1
    Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : job: I am sleeping 2
    Thread : Thread[main,5,main] :: message : main: I am tired of waiting
    Thread : Thread[main,5,main] :: message : main: Now I can quit!!!
    */
}

/**
 * Cancellation and Timeouts : Closing resources with finally.
 * */
class SimpleCoroutineCancelWithFinally {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                try {
                    repeat(1000) {
                        log("job: I am sleeping $it")
                        delay(500L)
                    }
                } finally {
                    log("job: I am running finally")
                }
            }
            delay(1300)
            log("main: I am tired of waiting")
            // cancel() throws CancellationException. That is the reason why cancel() method needs join() method to cancel a coroutine.
            // job.cancel()
            /*
            * Output of using job.cancel()
            * job: I am sleeping 0
            * job: I am sleeping 1
            * job: I am sleeping 2
            * main: I am tired of waiting
            * main: Now I can quit
            * job: I am running finally // With this line of output it is confirmed that the coroutine is not cancelled.
            *
            * */
            // job.join()
            /*
            * Output of using job.cancel() with job.join() method
            * job: I am sleeping 0
            * job: I am sleeping 1
            * job: I am sleeping 2
            * main: I am tired of waiting
            * job: I am running finally
            * main: Now I can quit
            *
            * */
            job.cancelAndJoin()
            /*
            * Output of using job.cancelAndJoin() method
            Thread : Thread[main,5,main] :: message : job: I am sleeping 0
            Thread : Thread[main,5,main] :: message : job: I am sleeping 1
            Thread : Thread[main,5,main] :: message : job: I am sleeping 2
            Thread : Thread[main,5,main] :: message : main: I am tired of waiting
            Thread : Thread[main,5,main] :: message : job: I am running finally
            Thread : Thread[main,5,main] :: message : main: Now I can quit
            * */
            log("main: Now I can quit")
        }
    }
}

/**
 * Cancellation and Timeouts | Run NonCancellable block - withContext and NonCancellable
 * */
class DelayCoroutineCancellationWithContextAndNonCancellable {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                try {
                    repeat(1000) {
                        log("job: I am sleeping $it")
                        delay(500L)
                    }
                } finally {
                    log("job: I am running finally inside")
                    withContext(NonCancellable) {
                        delay(3000L)
                        log("job: I have delayed to process of cancellation by 3 seconds")
                    }
                }
            }
            delay(1300L)
            log("main: I am tired of waiting")
            job.cancelAndJoin()
            log("main: Now I can quit")
            /*
            * Output
            * Thread : Thread[main,5,main] :: message : job: I am sleeping 0
            * Thread : Thread[main,5,main] :: message : job: I am sleeping 1
            * Thread : Thread[main,5,main] :: message : job: I am sleeping 2
            * Thread : Thread[main,5,main] :: message : main: I am tired of waiting
            * Thread : Thread[main,5,main] :: message : job: I am running finally inside
            * Thread : Thread[main,5,main] :: message : job: I have delayed to process of cancellation by 3 seconds
            * Thread : Thread[main,5,main] :: message : main: Now I can quit
            *
            * */
        }
    }
}

/**
 * Cancellation and Timeouts : Timeout - Fail
 * */
class SimpleCoroutineFailedTimeout {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* Close the coroutine in 1300 milliseconds */
            withTimeout(1300L) {
                repeat(1000) {
                    log("I am sleeping $it")
                    delay(500L)
                }
            }
        }
    }
    /*
    * Output
    Thread : Thread[main,5,main] :: message : I am sleeping 0
    Thread : Thread[main,5,main] :: message : I am sleeping 1
    Thread : Thread[main,5,main] :: message : I am sleeping 2
    Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
	at kotlinx.coroutines.TimeoutKt.TimeoutCancellationException(Timeout.kt:184)
	at kotlinx.coroutines.TimeoutCoroutine.run(Timeout.kt:154)
	at kotlinx.coroutines.EventLoopImplBase$DelayedRunnableTask.run(EventLoop.common.kt:508)
	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:284)
	at kotlinx.coroutines.DefaultExecutor.run(DefaultExecutor.kt:108)
	at java.base/java.lang.Thread.run(Thread.java:833)
    * */
}

/**
 * Cancellation and Timeout : Timeout - Success
 * */
class SimpleCoroutineTimeout {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val result = withTimeoutOrNull(1300L) {
                repeat(1000) {
                    log("I am sleeping $it...")
                    delay(500)
                }
                "Done" // Returning a value from withTimeoutOrNull to the result variable.
            }
            log("Result is $result")
        }

        /*
        * If the repeat block completes in time.
        * Output
        * Thread : Thread[main,5,main] :: message : I am sleeping 0...
        * Thread : Thread[main,5,main] :: message : Result is Done
        * */

        /*
        * If the repeat block doesn't complete in time.
        * Output
        Thread : Thread[main,5,main] :: message : I am sleeping 0...
        Thread : Thread[main,5,main] :: message : I am sleeping 1...
        Thread : Thread[main,5,main] :: message : I am sleeping 2...
        Thread : Thread[main,5,main] :: message : Result is null
        * */
    }
}

/**
 * Cancellation and Timeout : Asynchronous Timeout and Resources - Failed
 * */

private var acquired: Int = 0

class Resource {
    init {
        acquired++
    }

    fun close() = acquired--
}

class SimpleCoroutineAsynchronousTimeoutFailed {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runBlocking {
                repeat(1000) {
                    launch {
                        /* Trying to get an object of Resource after 60 seconds */
                        val resource = withTimeout(60) {
                            delay(50)
                            Resource()
                        }
                        resource.close()
                    }
                }
            }
            log("$acquired")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : 94
    * As the output is not zero this means that creating a Resource object is creating a memory leak.
    * */
}

/**
 * Cancellation and Timeout : Asynchronous Timeout and Resources - Success
 * */
class SimpleCoroutineAsynchronousTimeout {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runBlocking {
                repeat(1000) {
                    launch {
                        var resource: Resource? = null
                        try {
                            withTimeout(60) {
                                delay(50)
                                resource = Resource()
                            }
                        } finally {
                            resource?.close()
                        }
                    }
                }
            }
            log("$acquired")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : 0
    * As the timeout is zero this means that creating an object of Resource isn't create a memory leak.
    * */
}

/**
 * Composing Suspending Functions : Sequential by default
 * */
class SynchronousSuspendingFunctions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val time = measureTimeMillis {
                val one = doSomethingUsefulOne()
                val two = doSomethingUsefulTwo()
                log("Result is ${one + two}")
            }
            log("Completed in : $time")
        }

        suspend fun doSomethingUsefulOne(): Int {
            delay(1000L)
            return 12
        }

        suspend fun doSomethingUsefulTwo(): Int {
            delay(1000L)
            return 13
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Result is 25
    * Thread : Thread[main,5,main] :: message : Completed in : 2048
    * As the system took more than 2 seconds to complete the task, it determines that both the suspending
    * functions ran sequentially to return the result.
    * */
}

/**
 * Composing Suspending Functions : Concurrent using async
 * */
class ConcurrentSuspendingFunctions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val time = measureTimeMillis {
                val one = async { SynchronousSuspendingFunctions.doSomethingUsefulOne() }
                val two = async { SynchronousSuspendingFunctions.doSomethingUsefulTwo() }
                log("Result is ${one.await() + two.await()}")
            }
            log("Completed in $time milliseconds")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Result is 25
    * Thread : Thread[main,5,main] :: message : Completed in 1086 milliseconds
    * As the system took less than 2 seconds to complete the task, it determines that both the suspending
    * functions ran concurrently to return the result.
    * */
}

/**
 * Composing Suspending Functions : Lazily Started async
 * */
class LazyConcurrentSuspendingFunctions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val time = measureTimeMillis {
                val one = async(start = CoroutineStart.LAZY) { SynchronousSuspendingFunctions.doSomethingUsefulOne() }
                val two = async(start = CoroutineStart.LAZY) { SynchronousSuspendingFunctions.doSomethingUsefulTwo() }
                /*
                * Values used for start parameter of async or launch coroutine calls.
                * DEFAULT -- immediately schedules coroutine for execution according to its context;
                * LAZY -- starts coroutine lazily, only when it is needed;
                * ATOMIC -- atomically (in a non-cancellable way) schedules coroutine for execution according to its context;
                * UNDISPATCHED -- immediately executes coroutine until its first suspension point in the current thread.
                */
                one.start()
                two.start()
                /*
                * If you don't call start() on 'two' variable then it's suspending function
                * will execute sequentially.
                * Output
                * Thread : Thread[main,5,main] :: message : Result is 25
                * Thread : Thread[main,5,main] :: message : Completed in 2143 ms
                * */
                log("Result is ${one.await() + two.await()}")
            }
            log("Completed in $time ms")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Result is 25
    * Thread : Thread[main,5,main] :: message : Completed in 1138 ms
    * As the system took less than 2 seconds to complete the task, it determines that both the suspending
    * functions ran concurrently to return the result.
    * */
}

/**
 * Composing Suspending Functions : Async-Style Functions - Not Recommended.
 * */
class ConcurrentSuspendingFunctionsMergeWithMainDiscouraged {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val time = measureTimeMillis {
                val one = somethingUsefulOneAsync()
                val two = somethingUsefulTwoAsync()
                runBlocking {
                    log("Result is ${one.await() + two.await()}")
                }
            }
            log("Completed in $time ms")
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun somethingUsefulOneAsync() = GlobalScope.async {
            SynchronousSuspendingFunctions.doSomethingUsefulOne()
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun somethingUsefulTwoAsync() = GlobalScope.async {
            SynchronousSuspendingFunctions.doSomethingUsefulTwo()
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Result is 25
    * Thread : Thread[main,5,main] :: message : Completed in 1156 ms
    * */
}

/**
 * Composing Suspending Functions : Structured concurrency with Async - Recommended
 * */
class ConcurrentSuspendingFunctionsMergeWithMainRecommended {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val time = measureTimeMillis {
                log("Result is ${concurrentSum()}")
            }
            log("Completed in $time ms")
        }

        /* Here you can't define the context of coroutine. */
        private suspend fun concurrentSum(): Int = coroutineScope<Int> {
            log("Running from coroutineScope")
            val one = async { SynchronousSuspendingFunctions.doSomethingUsefulOne() }
            val two = async { SynchronousSuspendingFunctions.doSomethingUsefulTwo() }
            one.await() + two.await()
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Running from coroutineScope
    * Thread : Thread[main,5,main] :: message : Result is 25
    * Thread : Thread[main,5,main] :: message : Completed in 1039 ms
    * */
}

/**
 * Composing Suspending Functions : Structured concurrency with async - Exception
 * */
class ConcurrentSuspendingFunctionsMergeWithMainException {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val time = measureTimeMillis {
                try {
                    failedConcurrentSum()
                } catch (e: ArithmeticException) {
                    log("Computation failed with Arithmetic Exception")
                }
            }
            log("Completed in $time ms")
        }

        private suspend fun failedConcurrentSum(): Int = coroutineScope {
            val one = async<Int> {
                try {
                    delay(Long.MAX_VALUE)
                    42
                } finally {
                    log("First Child was cancelled")
                }
            }
            val two = async<Int> {
                log("Second child throws an exception")
                throw ArithmeticException()
            }
            one.await() + two.await()
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Second child throws an exception
    * Thread : Thread[main,5,main] :: message : First Child was cancelled
    * Thread : Thread[main,5,main] :: message : Computation failed with Arithmetic Exception
    * Thread : Thread[main,5,main] :: message : Completed in 26 ms
    * */
}

/**
 * Coroutine Context and Dispatchers : Dispatchers and Threads
 * */
class DispatchersAndThreadsIntroduction {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            launch {
                log("Default Dispatcher")
            }
            launch(Dispatchers.IO) {
                log("IO Dispatcher")
            }
            launch(Dispatchers.Unconfined) {
                log("Unconfined Dispatcher")
            }
            launch(newSingleThreadContext("MyOwnThread")) {
                log("Main MyOwnThread")
            }
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Unconfined Dispatcher
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : IO Dispatcher
    * Thread : Thread[MyOwnThread,5,main] :: message : Main MyOwnThread
    * Thread : Thread[main,5,main] :: message : Default Dispatcher
    * */
}

/**
 * Coroutine Context and Dispatchers : Confined vs Unconfined Dispatchers
 * */
class ConfinedAndUnconfinedDispatcherDifference {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            launch(Dispatchers.Unconfined) {
                log("Starting Coroutine with Unconfined Dispatcher")
                delay(1000L)
                log("Ending Coroutine with Unconfined Dispatcher")
            }
            launch {
                log("Starting Coroutine with Main Dispatcher")
                delay(1000L)
                log("Ending Coroutine with Main Dispatcher")
            }
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Starting Coroutine with Unconfined Dispatcher
    * Thread : Thread[main,5,main] :: message : Starting Coroutine with Main Dispatcher
    * Thread : Thread[kotlinx.coroutines.DefaultExecutor,5,main] :: message : Ending Coroutine with Unconfined Dispatcher
    * Thread : Thread[main,5,main] :: message : Ending Coroutine with Main Dispatcher
    * */
}

/**
 * Coroutine Context and Dispatchers : Debugging using logging
 * */
class DebugWithLogging {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            val a = async<Int> {
                delay(1000L)
                log("I am computing a piece of the answer")
                10
            }
            val b = async<Int> {
                delay(1500L)
                log("I am computing b piece of the answer")
                20
            }
            log("Result is ${a.await() + b.await()}")
        }
    }
    /*
    * Output
    Thread : Thread[main,5,main] :: message : I am computing a piece of the answer
    Thread : Thread[main,5,main] :: message : I am computing b piece of the answer
    Thread : Thread[main,5,main] :: message : Result is 30
    */
}

/**
 * Coroutine Context with Dispatchers : Jumping between Threads
 * */
class JumpingThreadsIntroduction {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) {
            newSingleThreadContext("Context1").use { context1 ->
                newSingleThreadContext("Context2").use { context2 ->
                    runBlocking(context1) {
                        log("Starting Context1")
                        withContext(context2) {
                            log("Working In Context2")
                        }
                        log("Ending Context1")
                    }
                }
            }
        }
    }
    /*
    * Output
    * Thread : Thread[Context1,5,main] :: message : Starting Context1
    * Thread : Thread[Context2,5,main] :: message : Working In Context2
    * Thread : Thread[Context1,5,main] :: message : Ending Context1
    * */
}

/**
 * Coroutine Context and Dispatchers : Job in the context
 * */
class JobInContextIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            log("My job is ${coroutineContext[Job]}")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : My job is BlockingCoroutine{Active}@5f8ed237
    * */
}

/**
 * Coroutine Context and Dispatchers : Children of Coroutine
 * */
class ChildCoroutineIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val parentCoroutine = launch {
                launch(Job()) {
                    log("job1: I run in my own Job and execute independently!")
                    delay(1000)
                    log("job1: I am not affected by the cancellation request on my parent coroutine")
                }
                launch {
                    delay(100)
                    log("job2: I am a child of the parentCoroutine")
                    delay(1000)
                    log("job2: I will not execute this line because my parent coroutine is cancelled")
                }
            }
            delay(500) // Terminating a parent coroutine after 500ms
            parentCoroutine.cancelAndJoin()
            delay(1000) // Waiting for the independent job to complete.
            log("main: Who has survived the cancellation request on parent coroutine")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : job1: I run in my own Job and execute independently!
    * Thread : Thread[main,5,main] :: message : job2: I am a child of the parentCoroutine
    * Thread : Thread[main,5,main] :: message : job1: I am not affected by the cancellation request on my parent coroutine
    * Thread : Thread[main,5,main] :: message : main: Who has survived the cancellation request on parent coroutine
    * */
}

/**
 * Coroutine Context and Dispatchers : Parental Responsibilities
 * */
class ParentalResponsibilityIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val request = launch {
                repeat(3) {
                    launch {
                        delay((it + 1) * 200L)
                        log("job$it: Coroutine $it is done")
                    }
                }
                log("I am done and, I don't explicitly join my children")
            }
            request.join()
            log("main: Now processing of the request coroutine is complete")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : I am done and, I don't explicitly join my children
    * Thread : Thread[main,5,main] :: message : job0: Coroutine 0 is done
    * Thread : Thread[main,5,main] :: message : job1: Coroutine 1 is done
    * Thread : Thread[main,5,main] :: message : job2: Coroutine 2 is done
    * Thread : Thread[main,5,main] :: message : main: Now processing of the request coroutine is complete
    * */
}

/**
 * Coroutine Context and Dispatchers : Naming Coroutines for Debugging.
 * */
class NamingCoroutine {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            log("main: Started")
            val v1 = async(CoroutineName("v1Coroutine")) {
                delay(1000L)
                log("async job :: v1 :: Working in v1Coroutine")
                252
            }
            val v2 = async(CoroutineName("v2Coroutine")) {
                delay(1000L)
                log("async job :: v2 :: Working in v2Coroutine")
                6
            }
            log("The answer of v1/v2 is ${v1.await() / v2.await()}")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : main: Started
    * Thread : Thread[main,5,main] :: message : async job :: v1 :: Working in v1Coroutine
    * Thread : Thread[main,5,main] :: message : async job :: v2 :: Working in v2Coroutine
    * Thread : Thread[main,5,main] :: message : The answer of v1/v2 is 42
    * */
}

/**
 * Coroutine Context and Dispatchers : Combining Context Elements
 * */
class CombineContextElements {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking<Unit> {
            launch(Dispatchers.Default + CoroutineName("Test")) {
                log("Working in Test Coroutine")
            }
        }
    }
    /*
    * Output
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : Working in Test Coroutine
    * */
}

/**
 * Coroutine Context and Dispatchers : Coroutine Scope
 * */
class Activity {
    private val mainScope = CoroutineScope(Dispatchers.Default + CoroutineName("ActivityScope"))
    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething() {
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 200L)
                log("Coroutine $i is done")
            }
        }
    }
}

class CoroutineScopeIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val activity = Activity()
            activity.doSomething()
            log("Coroutines Launched")
            delay(500L)
            log("Destroying Activity")
            activity.destroy()
            delay(1000L)
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Coroutines Launched
    * Thread : Thread[DefaultDispatcher-worker-2,5,main] :: message : Coroutine 0 is done
    * Thread : Thread[DefaultDispatcher-worker-2,5,main] :: message : Coroutine 1 is done
    * Thread : Thread[main,5,main] :: message : Destroying Activity
    * */
}

/**
 * Coroutine Context and Dispatchers : Thread-Local data -
 * This feature is basically used to pass data from one coroutine to another coroutine.
 * */
class ThreadLocalIntroduction {
    companion object {
        private val threadLocal = ThreadLocal<String>()

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            threadLocal.set("main")
            log("Pre-main, Thread Local Value : ${threadLocal.get()}")
            val job = launch(Dispatchers.Default + threadLocal.asContextElement("launch")) {
                log("Launch Started, Thread Local Value : ${threadLocal.get()}")
                yield()
                log("After yield, Thread Local Value : ${threadLocal.get()}")
            }
            job.join()
            log("Post Main")
        }
    }
    /*
    * Output
    * Thread : Thread[main,5,main] :: message : Pre-main, Thread Local Value : main
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : Launch Started, Thread Local Value : launch
    * Thread : Thread[DefaultDispatcher-worker-1,5,main] :: message : After yield, Thread Local Value : launch
    * Thread : Thread[main,5,main] :: message : Post Main
    * */
}