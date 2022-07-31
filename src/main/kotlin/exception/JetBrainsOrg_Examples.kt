package exception

import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext

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

/**
 * Coroutine Exceptions Handling - CoroutineExceptionHandler
 * */
class CoroutineExceptionHandlerExample {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
                println("$coroutineContext got exception ${throwable.message}")
            }
            val job = GlobalScope.launch(handler) {
                println("Throwing exception from launch")
                throw AssertionError()
            }
            val deferred = GlobalScope.async(handler) {
                println("Throwing exception from async")
                throw ArithmeticException()
            }
            joinAll(job, deferred)
        }
    }
    /*
    * Output
    * Throwing exception from launch
    * Throwing exception from async
    * [exception.CoroutineExceptionHandlerExample$Companion$main$1$invokeSuspend$$inlined$CoroutineExceptionHandler$1@17281e55, StandaloneCoroutine{Cancelling}@10431434, Dispatchers.Default] got exception null
    * */
}

/**
 * Coroutine Exceptions Handling - Cancellation and Exceptions - Job.cancel() don't cancel a parent coroutine
 * it does terminate the child coroutine.
 * */
class CancellationAndExceptionsExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                val child = launch {
                    try {
                        delay(Long.MAX_VALUE)
                    } finally {
                        println("Child is cancelled")
                    }
                }
                yield() // Because delay value is not defined so to complete the delay you have to use yield
                println("Cancelling Child")
                child.cancelAndJoin()
                println("Parent is not cancelled")
            }
            job.join()
        }
    }
    /*
    * Output - Without Yield
    * Cancelling Child
    * Parent is not cancelled
    *
    * Output - With Yield
    * Cancelling Child
    * Child is cancelled
    * Parent is not cancelled
    * */
}

/**
 * Coroutine Exceptions Handler - Cancellation and Exceptions - CoroutineExceptionHandler is not used
 * for child coroutines
 * */
class CoroutineExceptionHandlerForParent {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val handler = CoroutineExceptionHandler { _, throwable ->
                println("CoroutineExceptionHandler got ${throwable.message} exception")
            }
            val job = GlobalScope.launch(handler) {
                // The First Child
                launch {
                    try {
                        delay(Long.MAX_VALUE)
                    } finally {
                        withContext(NonCancellable) {
                            println("Children are cancelled, but exception is not handled until all children terminate")
                            delay(100)
                            println("The first child finished its non cancellable block")
                        }
                    }
                }
                // The Second Child
                launch {
                    delay(10)
                    println("Second Child throws an exception")
                    throw ArithmeticException()
                }
            }
            job.join()
        }
    }
    /*
    * Output
    * Second Child throws an exception
    * Children are cancelled, but exception is not handled until all children terminate
    * The first child finished its non cancellable block
    * CoroutineExceptionHandler got null exception
    * */
}

/**
 * Coroutine Exceptions Handling - Exceptions Aggregation - Additional exceptions (caused
 * by the child coroutines) are attached to the first exceptions.
 * */
class ExceptionsAggregationWithHandler {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val handler = CoroutineExceptionHandler { _, throwable ->
                println("CoroutineExceptionHandler caught ${throwable.message} exception")
            }
            val job = GlobalScope.launch(handler) {
                launch {
                    try {
                        delay(Long.MAX_VALUE)
                    } finally {
                        throw ArithmeticException()
                    }
                }
                launch {
                    delay(100L)
                    throw IOException()
                }
                delay(Long.MAX_VALUE)
            }
            job.join()
        }
    }
    /*
    * Output
    * CoroutineExceptionHandler caught null exception
    * */
}

/**
 * Coroutine Exception Handling - Exceptions Aggregation - Cancellation with CancellationException
 * */
class CancellationExceptionExample {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val handler = CoroutineExceptionHandler { _, throwable ->
                println("CoroutineExceptionHandler caught ${throwable.message} exception")
            }
            val job = GlobalScope.launch(handler) {
                val inner = launch {
                    launch {
                        launch {
                            throw IOException()
                        }
                    }
                }
                try {
                    inner.join()
                } catch (e: CancellationException) {
                    println("Rethrowing CancellationException with original cause")
                    throw e
                }
            }
            job.join()
        }
    }
    /*
    * Output
    * Rethrowing CancellationException with original cause
    * CoroutineExceptionHandler caught null exception
    * */
}

/**
 * Coroutine Exceptions Handling - Supervision - Supervision job - Exception is propagated only downwards
 * */
class SupervisionJobExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val superVisor = SupervisorJob()
            with(CoroutineScope(coroutineContext + superVisor)) {
                val handler = CoroutineExceptionHandler { _, throwable ->
                    println("CoroutineExceptionHandler caught ${throwable.message} exception")
                }
                val firstChild = launch(handler) {
                    println("The first child is failing")
                    throw AssertionError("The first child is cancelled")
                }
                val secondChild = launch(handler) {
                    firstChild.join()
                    println("The first child is cancelled: ${firstChild.isCancelled}")
                    try {
                        delay(Long.MAX_VALUE)
                    } finally {
                        println("The second child is cancelled because the supervisor got cancelled")
                    }
                }
                firstChild.join()
                println("Cancelling the supervisor")
                superVisor.cancel()
                secondChild.join()
            }
        }
    }
    /*
    * Output
    * The first child is failing
    * CoroutineExceptionHandler caught The first child is cancelled exception
    * The first child is cancelled: true
    * Cancelling the supervisor
    * The second child is cancelled because the supervisor got cancelled
    * */
}

/**
 * Coroutine Exceptions Handling - Supervision Scope
 * */
class SupervisionScopeExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            try {
                supervisorScope {
                    val child = launch {
                        try {
                            println("The child is sleeping")
                            delay(Long.MAX_VALUE)
                        } finally {
                            println("This child is cancelled")
                        }
                    }
                    // Give your child a chance to execute and print using yield
                    yield()
                    println("Throwing an exception from the scope")
                    throw AssertionError()
                }
            } catch (e: AssertionError) {
                println("Caught an assertion error")
            }
        }
    }
    /*
    * Output
    * The child is sleeping
    * Throwing an exception from the scope
    * This child is cancelled
    * Caught an assertion error
    * */
}