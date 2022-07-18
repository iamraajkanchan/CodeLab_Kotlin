package coroutines

import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
private val time: String = formatter.format(LocalDateTime.now())

private suspend fun getValue(): Double {
    println("Entering getValue() at $time")
    delay(3000)
    println("Leaving getValue() at $time")
    return Math.random()
}

fun main() {
    runBlocking {
        val num1 = async { getValue() }
        val num2 = async { getValue() }
        println("Result of num1 + num2 is ${num1.await() + num2.await()}")
    }
}