package classes

data class Student(val name: String? = null, var rollNo: Int? = null, var className: String? = null)

class Person(private val student: Student? = null) {
    init {
        displayStudentDetails()
    }

    private fun displayStudentDetails() {
        student?.also {
            println("Name: ${it.name}")
            println("Roll No: ${it.rollNo}")
            println("Class Name: ${it.className}")
        }
    }

    fun updateClassName(newClassName: String) {
        student?.className = newClassName
        displayStudentDetails()
    }

    fun updateRollNumber(newRollNo: Int) {
        student?.rollNo = newRollNo
        displayStudentDetails()
    }
}

/**
 * Classes - Constructors
 * */
class InitBlockExample(name: String) {

    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints $name")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            InitBlockExample("Raj Kanchan")
        }
    }
    /*
    * Output
    * First property: Raj Kanchan
    * First initializer block that prints Raj Kanchan
    * Second property: 11
    * Second initializer block that prints 11
    * */
}