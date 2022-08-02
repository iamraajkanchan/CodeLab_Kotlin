package collections

/**
 * Collections Overview - Collection Types
 * */
class CollectionTypes {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numbers = mutableListOf<String>("one", "two", "three", "four")
            for (number in numbers) print("$number ")
            println()
            println("Adding fine in numbers")
            numbers.add("five")
            numbers.forEach { print("$it ") }
            println()
        }
    }
    /*
    * Output
    * one two three four
    * Adding fine in numbers
    * one two three four five
    * */
}

/**
 * Collection Overview - Collection Types - Collection -
 * List and Set interfaces implements Collection interface
 * */
class ImmutableCollectionExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numberList = listOf<String>("one", "two", "four", "five", "one", "three")
            println("Printing List")
            printAll(numberList) // Printing a List of String
            val numberSet = setOf<String>("one", "two", "four", "five", "one", "three")
            println("Printing Set")
            printAll(numberSet) // Printing a Set of String
        }

        /**
         * printAll method is used to print elements of a List of Set.
         * @param strings can take List and Set type of variables.
         * */
        private fun printAll(strings: Collection<String>) {
            strings.forEach { print("$it ") }
            println()
        }
    }
    /*
    * Output
    * Printing List
    * one two four five one three
    * Printing Set
    * one two four five three
    * */
}

/**
 * Collection Overview - Collection Types - Collection -
 * List and Set interfaces implements Collection interface
 * */
class MutableCollectionExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val words = "A long time ago in a galaxy far far away".split(" ")
            val shortWords = mutableListOf<String>()
            words.getShortWordsTo(shortWords, 3)
            println(shortWords)
        }

        private fun Collection<String>.getShortWordsTo(shortWords: MutableCollection<String>, length: Int) {
            this.filterTo(shortWords) { it.length <= length }
            val articles = setOf<String>("a", "A", "an", "An", "the", "The")
            shortWords -= articles
        }
    }
    /*
    * Output
    * [ago, in, far, far]
    * */
}

/**
 * Collections Overview - Collection Types - List - Basic methods and concept of List
 * */
class BasicMethodOfListExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numbers = listOf<String>("one", "two", "three", "four")
            println("Number of elements: ${numbers.size}")
            println("Third element: ${numbers.get(2)}")
            println("Fourth element: ${numbers[3]}")
            println("Index of element \"two\": ${numbers.indexOf("two")}")
        }
    }
    /*
    * Output
    * Number of elements: 4
    * Third element: three
    * Fourth element: four
    * Index of element "two": 1
    * */
}

/**
 * Collections Overview - Collection Types - List -
 * */

data class Person(var name: String, var age: Int)
class ImmutableListExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val bob = Person("Bob Parson", 31)
            val people1 = listOf<Person>(Person("Adam Kane", 35), bob, bob)
            val people2 = listOf<Person>(Person("Adam Kane", 35), Person("Bob Parson", 31), bob)
            println(people1 == people2)
            bob.age = 32
            println("Age of Bob is modified")
            println(people1 == people2) // is not equal because the age of its second element is not modified
        }
    }
    /*
    * Output
    * true
    * Age of Bob is modified
    * false
    * */
}