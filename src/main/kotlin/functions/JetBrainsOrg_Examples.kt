package functions

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import java.lang.Math.floor

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

/**
 * Functions - Named Arguments - Infix Notations
 * */
class InfixNotationsExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            /* Here 2 is the firstArgument */
            /* Couldn't figure out the significance of */
            val result = 100 add 2
            println(result)
        }

        private infix fun Int.add(firstArgument: Int): Int {
            return firstArgument + 2
        }
    }
    /*
    * Output
    * 4
    * */
}

/**
 * High-Order functions and lambdas - High Order functions - Function Types -
 * Passing Anonymous Function as a parameter to another function.
 * */
class HighOrderFunctionsExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            joinTwoStrings("Hello") {
                it.uppercase()
            }
            joinTwoStrings("Love") {
                /* These print methods are not printing the output */
                println(it)
                it.uppercase()
            }
        }

        private fun joinTwoStrings(staticArgument: String, dynamicArgument: (aString: String) -> String): () -> String {
            /* Using the same name as the argument used in function, will give you Named Shadow Warning. */
            val argument = {
                /* These print methods are not printing the output */
                println("$staticArgument ${dynamicArgument.invoke("world!!!")}")
                "$staticArgument ${dynamicArgument.invoke("world!!!")}"
            }
            return argument
        }
    }
    /*
    * Output
    *
    * */
}

/**
 * High Order functions and lambdas - Raywender Lich - Combining data in Pairs
 * src - https://www.youtube.com/watch?v=-i5BGPG3Q5o&list=PL23Revp-82LKI2N0NeyTQhe4J5-5VVGT0&index=2
 * */

class CombiningDataInPairs {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fullName = Pair<String, String>("John", "Snow")
            val age = 31
            println("Using methods and properties provided by Pair")
            println("His name is ${fullName.first} ${fullName.second}")
            val (firstName, lastName) = fullName
            println("Using destructuring method on a Pair variable")
            println("His name is $firstName $lastName")
        }
    }
    /*
    * Output
    * Using methods and properties provided by Pair
    * His name is John Snow
    * Using destructuring method on a Pair variable
    * His name is John Snow
    * */
}


/**
 * High Order functions and lambdas - Raywender Lich - Combining data in Triples
 * src - https://www.youtube.com/watch?v=-i5BGPG3Q5o&list=PL23Revp-82LKI2N0NeyTQhe4J5-5VVGT0&index=2
 * */
class CombiningDataInTriples {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fullName = Pair<String, String>("Matt", "Storm")
            val person = Pair(fullName, 31)
            println("Using methods and properties of Pair")
            println("His name is ${person.first.first} ${person.first.second}. He is ${person.second} years old")
            val birthdayInDetails = Triple<Int, Int, Int>(16, 1, 1990)
            val personDetail = Pair(fullName, birthdayInDetails)
            println("Using methods and properties of Pair and Triplets")
            println("${personDetail.first.first} ${personDetail.first.second} is born in ${personDetail.second.first}-${personDetail.second.second}-${personDetail.second.third}")
            val (name, birthDay) = personDetail
            val (firstName, lastName) = name
            val (date, month, year) = birthDay
            println("Using destructuring methods of Pair and Triplets")
            println("$firstName $lastName is born in $date-$month-$year")
        }
    }
    /*
    * Output
    * Using methods and properties of Pair
    * His name is Matt Storm. He is 31 years old
    * Using methods and properties of Pair and Triplets
    * Matt Storm is born in 16-1-1990
    * */
}

/**
 * Functions - Generate Julian
 * */
class GeneratingJulianClass {
    private val JGREG = 15 + 31 * (10 + 12 * 1582)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val myInvalidJulianDate = toJulianInvalid(intArrayOf(2022, 9, 1))
            println(myInvalidJulianDate)
            val myValidJulianDate = toJulianAlgorithm(intArrayOf(2022, 9, 1))
            println(myValidJulianDate)
        }

        /**
         * Resource
         * https://www.rgagnon.com/javadetails/java-0506.html
         * */
        private fun toJulianInvalid(ymd: IntArray): Double {
            val myObject = GeneratingJulianClass()
            val year = ymd[0]
            val month = ymd[1]
            val day = ymd[2]

            var julianYear = year
            if (year < 0) {
                julianYear++
            }
            var julianMonth = month
            if (month > 2) {
                julianMonth++
            } else {
                julianYear--
                julianMonth += 13
            }
            var julian =
                kotlin.math.floor(365.25 * julianYear) + kotlin.math.floor(30.6001 * julianMonth) + day + 1720995.0
            if (day + 31 * (month + 12 * year) >= myObject.JGREG) {
                val ja = (0.01 * julianYear).toInt()
                julian += 2 - ja + (0.25 * ja)
            }
            return kotlin.math.floor(julian)
        }

        /**
         * Resource
         * https://quasar.as.utexas.edu/BillInfo/JulianDatesG.html
         * */
        private fun toJulianAlgorithm(ymd: IntArray): Double {
            val year = ymd[0]
            val month = ymd[1]
            val day = ymd[2]

            val a = year / 100
            val b = a / 4
            val c = 2 - a + b
            val e = 365.25 * (year + 4716)
            val f = 30.6001 * (month + 1)
            val julianDate = c + day + e + f - 1524.5
            return julianDate
        }

        /**
         * Resource
         * https://quasar.as.utexas.edu/BillInfo/JulianDatesG.html
         * */
        private fun toJulianAlgorithmNew(ymd: IntArray): Double {
            var year = ymd[0]
            var month = ymd[1]
            val day = ymd[2]

            if (month < 3) {
                year--
                month += 12
            }

            val a = year / 100
            val b = a / 4
            val c = 2 - a + b
            val e = 365.25 * (year + 4716)
            val f = 30.6001 * (month + 1)
            val julianDate = c + day + e + f - 1524.5
            return julianDate
        }
    }
}