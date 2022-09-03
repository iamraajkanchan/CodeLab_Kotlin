package functions

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

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
            println("His name is ${fullName.first} ${fullName.second} and he is $age old")
            val (firstName, lastName) = fullName
            println("Using destructuring method on a Pair variable")
            println("His name is $firstName $lastName and he is $age old")
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
    private val J_GREG = 15 + 31 * (10 + 12 * 1582)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val myInvalidJulianDate = toJulianInvalid(intArrayOf(2022, 9, 1))
            println(myInvalidJulianDate)
            val myValidJulianDate = toJulianAlgorithm(intArrayOf(2022, 9, 1))
            println(myValidJulianDate)
            val d = SimpleDateFormat("D")
            println(d.format(Date()))
            val julianDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val julianYear = Calendar.getInstance().get(Calendar.YEAR)
            println("$julianYear$julianDay")
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            val currentDay = Calendar.getInstance().get(Calendar.DATE)
            println("$currentYear/$currentMonth/$currentDay")
            val currentJulianDay = toJulianInvalid(intArrayOf(currentYear, currentMonth, currentDay))
            println("Current Julian Day : $currentJulianDay")
            // Do not use this method to identify a Julian Day
            val tempJulianDay = toJulianAlgorithmNew(intArrayOf(currentYear, currentMonth, currentDay))
            println("Temp Julian Day : $tempJulianDay")
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
            if (day + 31 * (month + 12 * year) >= myObject.J_GREG) {
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

/**
 * Operator Overloading - Unary Operations - Increments and Decrements
 * */
class OperatorOverloadingWithIncrementsDecrements {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            this::incMinutes.invoke() // This is similar to call the function incMinutes()
            this::incHours.invoke() // This is similar to call the function incHours()
            /* If you don't invoke the function it won't execute. */
            this::incDays.invoke() // This is similar to call the function incDays()
            this::decHours.invoke()
            this::decMinutes.invoke()
        }

        private fun incHours() {
            operator fun LocalTime.inc(): LocalTime = plusHours(1L)
            var time = LocalTime.now()
            println("LocalTime without using increment operator :: time : $time")
            time++
            // Note: If you do not define the inc() operator function above, you will get compile time error if you use '++' operator.
            println("Incrementing hour of LocalTime using post increment operator :: time : $time")
            println("Incrementing hour of LocalTime using pre increment operator :: time : ${++time}")
        }

        private fun decHours() {
            operator fun LocalTime.dec(): LocalTime = minusHours(1L)
            var time = LocalTime.now()
            println("LocalTime without using decrement operator :: time : $time")
            time--
            // Note: If you do not define the dec() operator function above, you will get compile time error if you use '--' operator.
            println("Decrementing hour of LocalTime using post decrement operator :: time : $time")
            println("Decrementing hour of LocalTime using pre decrement operator :: time : ${--time}")
        }

        private fun incMinutes() {
            operator fun LocalTime.inc(): LocalTime = plusMinutes(10L)
            var time = LocalTime.now()
            println("LocalTime without using increment operator :: time : $time")
            time++
            // Note: If you do not define the inc() operator function above, you will get compile time error if you use '++' operator.
            println("Incrementing minutes of LocalTime using post increment operator :: time : $time")
            println("Incrementing minutes of LocalTime using pre increment operator :: time : ${++time}")
        }

        private fun decMinutes() {
            operator fun LocalTime.dec(): LocalTime = minusMinutes(10L)
            var time = LocalTime.now()
            println("LocalTime without using decrement operator :: time : $time")
            time--
            // Note: If you do not define the dec() operator function above, you will get compile time error if you use '--' operator.
            println("Decrementing minutes of LocalTime using post decrement operator :: time : $time")
            println("Decrementing minutes of LocalTime using pre decrement operator :: time : ${--time}")
        }

        /**
         * Reference - https://www.baeldung.com/java-add-hours-date
         * */
        private fun incDays() {
            operator fun Date.inc(): Date {
                val calendar = Calendar.getInstance()
                calendar.time = Date()
                calendar.add(Calendar.HOUR_OF_DAY, 24)
                return calendar.time
            }

            var day = Date()
            println("Date without using increment operator :: day : $day")
            day++
            // Note: If you do not define the inc() operator function above, you will get compile time error if you use '++' operator.
            println("Incrementing day of Date using post increment operator :: day : $day")
            println("Incrementing day of Date using pre increment operator :: day : ${++day}")
        }
    }
    /*
    * Output
    * Localtime without using increment operator :: time : 14:01:59.727171400
    * Incrementing hour of Localtime using post increment operator :: time: 15:01:59.727171400
    * Incrementing hour of Localtime using pre increment operator :: time 16:01:59.727171400
    * Localtime without using increment operator :: time : 14:01:59.729166400
    * Incrementing minutes of Localtime using post increment operator :: time: 14:11:59.729166400
    * Incrementing minutes of Localtime using pre increment operator :: time 14:21:59.729166400
    * Date without using increment operator :: day : Fri Sep 02 14:01:59 IST 2022
    * Incrementing day of Date using post increment operator :: day : Sat Sep 03 14:01:59 IST 2022
    * Incrementing day of Date using pre increment operator :: day : Sat Sep 03 14:01:59 IST 2022
    * LocalTime without using decrement operator :: time : 19:02:14.696268100
    * Decrementing minutes of LocalTime using post decrement operator :: time : 18:52:14.696268100
    * Decrementing minutes of LocalTime using pre decrement operator :: time : 18:42:14.696268100
    * */
}

/**
 * Operator Overloading - Unary Operations - Unary Prefix Operators
 * */
class OperatorOverloadingUnaryOperations {
    data class Point(val x: Int, val y: Int)
    data class Confirm(val isValidMobile: Boolean, val isValidOtp: Boolean)
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            this::convertIntegerToPositive.invoke()
            this::convertIntegerToNegative.invoke()
            this::invertBooleanValue.invoke()
        }

        private fun convertIntegerToPositive() {
            operator fun Point.unaryPlus() = Point(+x, +y)
            val point = Point(-10, -3)
            println("Object of Point Class without using unary plus operator :: point : $point")
            /* Unary plus operation is not working */
            println("Object of Point Class with unary plus operator :: point : ${+point}")
            // Note: If you do not define the unaryPlus operator function above, you will get compile time error if you use '+' operator.
        }

        private fun convertIntegerToNegative() {
            operator fun Point.unaryMinus() = Point(-x, -y)
            val point = Point(10, 3)
            println("Object of Point Class without using unary minus operator :: point : $point")
            // Unary Minus Operation is Working
            println("Object of Point Class with unary minus operator :: point : ${-point}")
            // Note: If you do not define the unaryMinus operator function above, you will get compile time error if you use '-' operator.
        }

        private fun invertBooleanValue() {
            operator fun Confirm.not() = Confirm(!isValidMobile, !isValidOtp)
            val confirmObject = Confirm(isValidMobile = true, isValidOtp = false)
            println("Object of Confirm Class without using unary not operator :: confirmObject : $confirmObject")
            // Unary Not Operation is Working
            println("Object of Confirm Class using unary not operator :: confirmObject : ${!confirmObject}")
            // Note: If you do not define the unary not operator function above, you will get compile time error if you use '!' operator.
        }
        /*
        * Output
        * Object of Point Class without using unary plus operator :: point : Point(x=-10, y=-3)
        * Object of Point Class with unary plus operator :: point : Point(x=-10, y=-3)
        * Object of Point Class without using unary minus operator :: point : Point(x=10, y=3)
        * Object of Point Class with unary minus operator :: point : Point(x=-10, y=-3)
        * Object of Confirm Class without using unary not operator :: confirmObject : Confirm(isValidMobile=true, isValidOtp=false)
        * Object of Confirm Class using unary not operator :: confirmObject : Confirm(isValidMobile=false, isValidOtp=true)
        * */
    }
}

/**
 * Operator Overloading - Binary Operations - Arithmetic Operators
 * */
class OperatorOverloadingArithmeticOperations {
    class CalculatingValues(val number: Int)
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            this::addOperation.invoke()
            this::subtractOperation.invoke()
            this::multiplyOperation.invoke()
            this::divideOperation.invoke()
            this::remainderOperation.invoke()
            this::rangeOperation.invoke()
        }

        private fun addOperation() {
            operator fun CalculatingValues.plus(argument: Int) = CalculatingValues(number + argument)
            val myObject = CalculatingValues(10)
            // myObject.plus(1) This operation doesn't make any difference
            println("Value of the property of CalculatingValues class before using plus operator function :: number : ${myObject.number}")
            println(
                "Value of the property of CalculatingValues class after using plus operator function :: number : ${
                    myObject.plus(1).number
                }"
            )
        }

        private fun subtractOperation() {
            operator fun CalculatingValues.minus(argument: Int) = CalculatingValues(number - argument)
            val myObject = CalculatingValues(10)
            // myObject.minus(1) // This operation doesn't make any difference
            println("Value of the property of CalculatingValues class before using minus operator function :: number : ${myObject.number}")
            println(
                "Value of the property of CalculatingValues class after using minus operator function :: number : ${
                    myObject.minus(1).number
                }"
            )
        }

        private fun multiplyOperation() {
            operator fun CalculatingValues.times(argument: Int) = CalculatingValues(number * argument)
            val myObject = CalculatingValues(10)
            // myObject.times(3) // This operation doesn't make any difference
            println("Value of the property of CalculatingValues class before using times operator function :: number : ${myObject.number}")
            println(
                "Value of the property of CalculatingValues class after using times operator function :: number : ${
                    myObject.times(3).number
                }"
            )
        }

        private fun divideOperation() {
            operator fun CalculatingValues.div(argument: Int) = CalculatingValues(number / argument)
            val myObject = CalculatingValues(10)
            // myObject.div(5) // This operation doesn't make any difference
            println("Value of the property of CalculatingValues class before using div operator function :: number : ${myObject.number}")
            println(
                "Value of the property of CalculatingValues class after using div operator function :: number : ${
                    myObject.div(5).number
                }"
            )
        }

        private fun remainderOperation() {
            operator fun CalculatingValues.rem(argument: Int) = CalculatingValues(number % argument)
            val myObject = CalculatingValues(10)
            // myObject.rem(3) // This operation doesn't make any difference
            println("Value of the property of CalculatingValues class before using rem operator function :: number : ${myObject.number}")
            println(
                "Value of the property of CalculatingValues class after using rem operator function :: number : ${
                    myObject.rem(3).number
                }"
            )
        }

        private fun rangeOperation() {
            operator fun CalculatingValues.rangeTo(argument: Int) = number..argument
            val myObject = CalculatingValues(10)
            println(
                "Value of the property of CalculatingValues class before using rangeTo operator function :: ${
                    myObject.rangeTo(15)
                }"
            )
        }
        /*
        * Output
        * Value of the property of CalculatingValues class before using plus operator function :: number : 10
        * Value of the property of CalculatingValues class after using plus operator function :: number : 11
        * Value of the property of CalculatingValues class before using minus operator function :: number : 10
        * Value of the property of CalculatingValues class after using minus operator function :: number : 9
        * Value of the property of CalculatingValues class before using times operator function :: number : 10
        * Value of the property of CalculatingValues class after using times operator function :: number : 30
        * Value of the property of CalculatingValues class before using div operator function :: number : 10
        * Value of the property of CalculatingValues class after using div operator function :: number : 2
        * Value of the property of CalculatingValues class before using rem operator function :: number : 10
        * Value of the property of CalculatingValues class after using rem operator function :: number : 1
        * Value of the property of CalculatingValues class before using rangeTo operator function :: 10..15
        * */
    }
}

/**
 * Operator Overloading - Binary Operations - in Operator
 * */
class OperatorOverloadingInOperations {
    class TempClass(val char: Char)
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            this::doExist.invoke()
            this::doNotExist.invoke()
        }

        private fun doExist() {
            operator fun TempClass.contains(chars: CharArray): Boolean = char in chars
            val tempObject = TempClass('e')
            val comparingRange = charArrayOf('a', 'b', 'f', 'g', 'e')
            println("Result of contains operator function on object of TempClass :: ${comparingRange in tempObject}")
        }

        private fun doNotExist() {
            operator fun TempClass.contains(chars: CharArray): Boolean = char in chars
            val tempObject = TempClass('e')
            val comparingRange = charArrayOf('a', 'b', 'f', 'g', 'e')
            println("Result of not contains operator function on object of TempClass :: ${comparingRange !in tempObject}")
        }
    }
    /*
    * Output
    * Result of contains operator function on object of TempClass :: true
    * Result of not contains operator function on object of TempClass :: false
    * */
}