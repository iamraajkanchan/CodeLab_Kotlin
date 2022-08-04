package classes

data class Student(val name: String? = null, val rollNo: Int? = null, val className: String? = null)

class Person(private val student: Student? = null) {
    init {
        displayDetails()
    }
    private fun displayDetails() {
        student?.also {
            println("Name: ${it.name}")
            println("Roll No: ${it.rollNo}")
            println("Class Name: ${it.className}")
        }
    }
}

/**
 * Classes - Constructors
 * */
class InitBlockExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val john = Student("John Doe", 22, "XII")
            val person = Person(john)
        }
    }
}