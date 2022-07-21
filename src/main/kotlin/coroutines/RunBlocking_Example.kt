package coroutines

import kotlinx.coroutines.*

class FirstRunBlocking {
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

class SecondRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            println("Name of the thread in runBlocking: ${Thread.currentThread()}")
            launch {
                println("Name of the thread in launch: ${Thread.currentThread()}")
                /* You cannot call doWork method if it is a non-static method */
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

class ThirdRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* As doWork is defined in the same thread i.e. main so the code inside doWork method is completed first and then println method is executed */
            doWork()
            println("World!!!")
        }

        private suspend fun doWork() {
            delay(1000L)
            println("Hello")
        }
    }
}

class FourthRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            /* launch is a child coroutine of runBlocking */
            val job = launch {
                doWork()
            }
            delay(1200L)
            job.join() // waits for the child coroutine to complete
            println("World!!!")
        }

        private suspend fun doWork() {
            delay(1000L)
            println("Hello")
        }
    }
}

class FifthRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    println("job: I am sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L)
            println("main: I am tired of waiting")
            job.cancel()
            job.join()
            /* Couldn't figure out the role of join method after cancel method. The result is same with or without the join method */
            println("main: Now I can quit")
        }
    }
}

class SixthRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    println("job: I am sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L)
            println("main: I am tired of waiting")
            job.cancelAndJoin()
            println("main: Now I can quit")
        }
    }
}

class SeventhRunBlocking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I am sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L)
            println("main: I am tired of waiting...")
        }
    }
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
            /* Even after the cancel and join method the child coroutine continues to execute the line of cold within */
            job.join()
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