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

class CoroutineScopeAsync {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            CoroutineScope(Dispatchers.Default).async {
                val num1 = getValue()
                val num2 = getValue()
                println("The result of num1 + num2 is ${num1 + num2}")
            }
        }
    }
}

class CoroutineScopeLaunch {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            CoroutineScope(Dispatchers.Default).launch {
                val num1 = getValue()
                val num2 = getValue()
                println("The result of num1 + num2 is ${num1 + num2}")
            }
        }
    }
}