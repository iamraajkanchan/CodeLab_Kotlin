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