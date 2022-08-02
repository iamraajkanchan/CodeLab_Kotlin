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

/**
 * Collections Overview - Collection Types - List -
 * */
class MutableListExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numbers = mutableListOf<String>("One", "Two", "Three")
            numbers.forEach { print("$it ") }
            println()
            println("Adding \"Four\"")
            numbers.add("Four")
            println("Removing \"One\"")
            numbers.remove("One")
            println("Adding \"Six\"")
            numbers.add("Six")
            println("Removing \"Three\"")
            numbers.removeAt(1)
            numbers.forEach { print("$it ") }
            println()
        }
    }
    /*
    * Output
    * One Two Three
    * Adding "Four"
    * Removing "One"
    * Adding "Six"
    * Removing "Three"
    * Two Four Six
    * */
}

/**
 * Collection Overview - Collection Type - Set
 * */
class ImmutableSetExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numberSet = setOf<Int>(1, 2, 3, 4)
            val uniqueNumberSet = setOf<Int>(3, 2, 1, 4, 1, 2, 3)
            numberSet.forEach { print("$it ") }
            println()
            uniqueNumberSet.forEach { print("$it ") }
            println()
            if (numberSet.contains(1)) println("1 is in the numberSet")
            println("numberSet and uniqueNumberSet are same: ${numberSet == uniqueNumberSet}")
        }
    }
    /*
    * Output
    * 1 2 3 4
    * 3 2 1 4
    * 1 is in the numberSet
    * numberSet and uniqueNumberSet are same: true
    * */
}

/**
 * Collection Overview - Collection Type - Set
 * */
class MutableSetExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val firstNumberSet = mutableSetOf<Int>(1, 2, 3, 4)
            val secondNumberSet = mutableSetOf<Int>(4, 3, 2, 1)
            println(firstNumberSet.first() == secondNumberSet.first())
            println(firstNumberSet.first() == secondNumberSet.last())
            firstNumberSet.add(4)
            secondNumberSet.add(1)
            println(firstNumberSet.first() == secondNumberSet.first())
            println(firstNumberSet.first() == secondNumberSet.last())
            firstNumberSet.filterTo(secondNumberSet) { it % 2 == 0 }
            println(firstNumberSet == secondNumberSet)
            firstNumberSet.mapTo(secondNumberSet) { it * 2 }
            firstNumberSet.forEach { print("$it ") }
            println()
            secondNumberSet.forEach { print("$it ") }
            println()
        }
    }
    /*
    * Output
    * false
    * true
    * false
    * true
    * true
    * 1 2 3 4
    * 4 3 2 1 6 8
    * */
}

/**
 * Collection Overview - Collection Types - Map
 * */
class ImmutableMapExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numbersMap = mapOf<String, Int>("key1" to 1, "key2" to 2, "key3" to 3)
            println("All Keys ${numbersMap.keys}")
            println("All Values ${numbersMap.values}")

            if ("key2" in numbersMap) println("Value by \"key2\": ${numbersMap["key2"]}")
            if (1 in numbersMap.values) println("The value 1 is in the numbersMap")
            if (numbersMap.containsValue(1)) println("The value 1 is in the numbersMap")
        }
    }
    /*
    * Output
    * All Keys [key1, key2, key3]
    * All Values [1, 2, 3]
    * Value by "key2": 2
    * The value 1 is in the numbersMap
    * The value 1 is in the numbersMap
    * */
}

/**
 * Collection Overview - Collection Type - Map - Convenient way to initialize a map
 * */
class MutableMapExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numbersMap = mutableMapOf<String, Int>().apply {
                this["key1"] = 1
                this["key2"] = 2
                this["key3"] = 3
                this["key4"] = 4
            }
            println("Initial numbersMap $numbersMap")
            println("Adding \"key5\" in numbersMap")
            numbersMap.put("key5", 5)
            println("Adding \"key6\" in numbersMap")
            numbersMap["key6"] = 6
            println("Modified numbersMap $numbersMap")
            val unOrderedNumbersMap = mutableMapOf<String, Int>().apply {
                this["key3"] = 3
                this["key1"] = 1
                this["key4"] = 4
                this["key2"] = 2
                this["key6"] = 6
                this["key5"] = 5
            }
            println("The numbersMap and unOrderedNumbersMap are equal : ${numbersMap == unOrderedNumbersMap}")
        }
    }
    /*
    * Output
    * Initial numbersMap {key1=1, key2=2, key3=3, key4=4}
    * Adding "key5" in numbersMap
    * Adding "key6" in numbersMap
    * Modified numbersMap {key1=1, key2=2, key3=3, key4=4, key5=5, key6=6}
    * The numbersMap and unOrderedNumbersMap are equal : true
    * */
}