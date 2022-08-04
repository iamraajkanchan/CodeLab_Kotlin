package classes

/**
 * You can use a trailing comma when you declare properties
 * */
data class GenericStudent(val name: String? = null, var rollNo: Int? = null, var className: String? = null)

class GenericPerson(private val student: GenericStudent? = null) {
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
    * First property: Raj Kanchan.
    * First initializer block that prints Raj Kanchan.
    * Second property: 11.
    * Second initializer block that prints 11
    * */
}

/**
 * Classes - Constructors - Secondary Constructors
 * */
class SecondaryConstructorPerson(val name: String) {
    val pets: MutableList<SecondaryConstructorPet> = mutableListOf()
    private val children: MutableList<SecondaryConstructorPerson> = mutableListOf()

    /*As you have used primary constructor you have to delegate it with the parameter through secondary constructor */
    constructor(name: String, parent: SecondaryConstructorPerson) : this(name) {
        children.add(parent)
    }
}

class SecondaryConstructorPet(val breed: String) {
    /*As you have used primary constructor you have to delegate it with the parameter through secondary constructor */
    constructor(breed: String, owner: SecondaryConstructorPerson) : this(breed) {
        owner.pets.add(this)
    }
}

class SecondaryConstructorsExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            /* Steven Anderson doesn't have a pet */
            val anderson = SecondaryConstructorPerson("Steven Anderson")
            /* John Anderson have a pet */
            val john = SecondaryConstructorPerson("John Anderson", anderson)
            /* John Anderson have a cat */
            val cat = SecondaryConstructorPet("Cat", john)
            println("${john.name} son of ${anderson.name}, has a ${cat.breed}.")
        }
    }
    /*
    * Output
    * John Anderson son of Steven Anderson, has a Cat.
    * */
}

