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