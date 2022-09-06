package destructuring

/**
 * Destructuring Declarations - Basic Destructuring
 * */
class BasicDestructuring {
    data class Person(val name: String, val age: Int)
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val teacher = Person("Claire Mathias", 24)
            /* To de-structure the properties of a class ma*/
            val (name, age) = teacher
            println("Name of the teacher is $name, who is just $age year old.")
        }
    }
    /*
    * Output
    * Name of the teacher is Claire Mathias, who is just 24 year old.
    * */
}