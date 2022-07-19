package coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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