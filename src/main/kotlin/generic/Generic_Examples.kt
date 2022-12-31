package functions.generic

/**
 * Generic Functions -
 * 1. Writing a Generic Print Function that Prints a Value of Any Type.
 * 2. Writing a Generic Return Function that Returns a Value of Any Type.
 * (Causes Error - Need to figure out how to do it?)
 * */

class GenericFunctionIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            printValues(1)
            printValues("Hello World")
            printValues(20.0)
            printValues(true)

            println(returnValueType(2.0)) // Causes Error.
            println(returnValueType(true))
            println(returnValueType(1))
            println(returnValueType("Chinki Minki"))
        }

        private fun <T> printValues(value: T) {
            println("GenericFunctionIntroduction :: printValues :: $value")
        }

        private fun <T> returnValueType(value: T): T {
            return when (value) {
                is String -> "$value is String"
                is Int -> "$value is Integer"
                is Double -> "$value is Double"
                is Float -> "$value is Float"
                is Boolean -> "$value is Boolean"
                else -> "$value has invalid type"
            } as T
        }
    }
    /*
    * Output
    * GenericFunctionIntroduction :: printValues :: 1
    * GenericFunctionIntroduction :: printValues :: Hello World
    * GenericFunctionIntroduction :: printValues :: 20.0
    * GenericFunctionIntroduction :: printValues :: true
    * */
}

/**
 * Generic Class -
 * 1. Creating a Generic Producer (Extend) Class - This means you can read with this class but couldn't write
 * */
class ExampleOfOutGeneric<out T>(private val value: T) {

}

/**
 * Generic Class
 * 1. Creating a Generic Consumer (Super) Class - This means you can write with this class but couldn't read.
 * */
class ExampleOfInGeneric<in T>(private val value: T) {

}