package functions

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow

/**
 * Functions - Default arguments
 * */
class FunctionWithDefaultArguments {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            printValues(0) {
                println("Testing the bar argument")
            }
            printValues(1) {
                println("Testing the baz argument")
            }
            printValues(qux = {
                println("Testing the qux argument")
            })
        }

        /* Passing an anonymous function (lambda function) into the argument. */
        private fun printValues(bar: Int = 0, baz: Int = 1, qux: () -> Unit) {
            println("FunctionWithDefaultArguments :: printValues :: bar : $bar")
            println("FunctionWithDefaultArguments :: printValues :: baz : $baz")
            qux.invoke() // Use the invoke method
        }
    }
    /*
    * Output
    * FunctionWithDefaultArguments :: printValues :: bar : 0
    * FunctionWithDefaultArguments :: printValues :: baz : 1
    * Testing the bar argument
    * FunctionWithDefaultArguments :: printValues :: bar : 1
    * FunctionWithDefaultArguments :: printValues :: baz : 1
    * Testing the baz argument
    * FunctionWithDefaultArguments :: printValues :: bar : 0
    * FunctionWithDefaultArguments :: printValues :: baz : 1
    * Testing the qux argument
    * */
}

/**
 * Functions - Named Arguments - vararg with asFlow
 * */
class FunctionsWithVarArgMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val job = launch(Dispatchers.IO) {
                launch {
                    foo("Hello Kotlin", "Hello Android")
                }
                launch {
                    foo("Hello Dart", "Hello Flutter")
                }
            }
            job.join()
            job.cancel()
            println("${Thread.currentThread().name} coroutine is completed")
        }

        private suspend fun foo(vararg text: String) {
            /* Do not use iterator to print values of vararg argument */
            text.asFlow().collect {
                println(it)
            }
        }
    }
    /*
    * Output
    * Hello Kotlin
    * Hello Android
    * Hello Dart
    * Hello Flutter
    * main coroutine is completed
    * */
}

/**
 * Functions - Named Arguments - Variable number of arguments (varargs)
 * */
class FunctionWithVarArgsAndGenerics {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val list = asList(1, 2, 3)
            list.forEach {
                println(it)
            }
            repeat(100) {
                print("=")
            }
            println()
            val a = arrayOf("Kotlin", "Java", "Android")
            val listWithString = asList(*list.toTypedArray(), *a)
            listWithString.forEach {
                println(it)
            }
        }

        private fun <T> asList(vararg ts: T): List<T> {
            val result = ArrayList<T>()
            for (t in ts) {
                result.add(t)
            }
            return result
        }
    }
    /*
    * Output
    * 1
    * 2
    * 3
    * ====================================================================================================
    * 1
    * 2
    * 3
    * Kotlin
    * Java
    * Android
    * */
}