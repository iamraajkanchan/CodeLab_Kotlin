package coroutines

import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch {
        delay(1000)
        println("Hello ${Thread.currentThread()}")
    }
}