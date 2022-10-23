// Below add function is an algorithm
private fun add(x: Int, y: Int) = x + y

// Below example is also an example of Algorithm

val hourInMillis = 24 * 60 * 60 * 1000

class ExamplesImplementation {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("2 + 3 = ${add(2, 3)}")
            println("Milliseconds in a day : $hourInMillis")
        }
    }
}

/**
 * Output
 * 2 + 3 = 5
 * Milliseconds in a day : 86400000
 * */
