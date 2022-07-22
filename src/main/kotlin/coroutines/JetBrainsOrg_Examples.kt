package coroutines

import kotlinx.coroutines.*

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
 * Cancellation and Timeouts : Making Computation code Cancellable - Failed Attempt
 * */
class SimpleCoroutineWithFailedCancelAttempt {
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

class EighthRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 10) {
                    if (System.currentTimeMillis() > nextPrintTime) {
                        println("job: I am sleeping ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            println("main: I am tired of waiting")
            job.cancel()
            job.join()
            /* Even after the cancel and join method the child coroutine continues to execute the line of cold within */
            println("main: Now I can quit")
        }
    }
}

class NinthRunBlocking {
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
                        println("job: I am sleeping ${i++}")
                        nextTime += 500L
                    }
                }
            }
            delay(1300L)
            println("main: I am tired of waiting")
            job.cancelAndJoin()
            println("main: Now I can quit")
        }
    }
}

class TenthRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            /* If launch the coroutine on Main thread, then the cancel() of the job doesn't work*/
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) {
                    if (System.currentTimeMillis() > nextPrintTime) {
                        println("job: I am sleeping ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            println("main: I am tired of waiting")
            /* Cancels the coroutine without join() */
            job.cancel()
            println("main: Now I can quit!!!")
        }
    }
}

/**
 * Cancellation and Timeouts | Closing resources with finally.
 * */
class EleventhRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                try {
                    repeat(1000) {
                        println("job: I am sleeping $it")
                        delay(500L)
                    }
                } finally {
                    println("job: I am running finally")
                }
            }
            delay(1300)
            println("main: I am tired of waiting")
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
            * job: I am sleeping 0
            * job: I am sleeping 1
            * job: I am sleeping 2
            * main: I am tired of waiting
            * job: I am running finally
            * main: Now I can quit
            *
            * */
            println("main: Now I can quit")
        }
    }
}

/**
 * Cancellation and Timeouts | Run NonCancellable block.
 * */
class TwelfthRunBlocking {
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