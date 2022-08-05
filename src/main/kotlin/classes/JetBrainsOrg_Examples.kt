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
            val initBlockExample = InitBlockExample("Raj Kanchan")
            println(initBlockExample.firstProperty)
            println(initBlockExample.secondProperty)
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

/**
 * Classes - Constructors - Even if the class has no primary constructor, the delegation still happens implicitly,
 * and the initializer blocks are still executed
 * */
class InitBlockExampleImplicitDelegate {
    init {
        println("Init Block")
    }

    constructor(i: Int) {
        println("Constructor $i")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            InitBlockExampleImplicitDelegate(1)
        }
    }
    /*
    * Output
    * Init Block
    * Constructor 1
    * */
}

/**
 * Classes - Abstract Classes - When you are using an abstract class you don't have to use 'open' keyword to inherit
 * */
abstract class AbstractExamplePolygon {
    abstract fun draw()
}

class AbstractExampleRectangle : AbstractExamplePolygon() {
    override fun draw() {
        println("Drawing in AbstractExampleRectangle")
    }
}

open class OpenExamplePolygon {
    open fun draw() {
        println("Drawing in OpenExamplePolygon")
    }
}

abstract class AbstractExampleWildShape : OpenExamplePolygon() {
    abstract override fun draw()
}

class AbstractClassExample : AbstractExampleWildShape() {
    override fun draw() {
        println("Drawing in AbstractClassExample")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val abstractExampleRectangle = AbstractExampleRectangle()
            abstractExampleRectangle.draw()
            val openExamplePolygon = OpenExamplePolygon()
            openExamplePolygon.draw()
            val abstractClassExample = AbstractClassExample()
            abstractClassExample.draw()
        }
    }
    /*
    * Output
    * Drawing in AbstractExampleRectangle
    * Drawing in OpenExamplePolygon
    * Drawing in AbstractClassExample
    * */
}

/**
 * Inheritance - Overriding Methods + Overriding Properties
 * */
/* Declare the class with 'open' modifier otherwise the class be final, and you won't be able to extend */
open class InheritanceExampleShape {

    /* Declare the property with 'open' modifier otherwise the function be final,
    and you won't be able to override it */
    open val vertexCount: Int = 0

    /* Declare the function with 'open' modifier otherwise the function be final,
    and you won't be able to override it */
    open fun draw() {
        println("Draw function in InheritanceExampleShape")
    }

    fun fill() {
        println("Fill function in InheritanceExampleShape")
    }

    init {
        println("InheritanceExampleShape :: vertexCount : $vertexCount")
    }
}

class InheritanceExampleCircle : InheritanceExampleShape() {
    override var vertexCount: Int = 0
    override fun draw() {
        println("Draw function in InheritanceExampleCircle")
    }

    init {
        println("InheritanceExampleCircle :: vertexCount : $vertexCount")
    }
}

open class InheritanceExampleRectangle : InheritanceExampleShape() {
    override val vertexCount: Int = 4
    final override fun draw() {
        println("Draw function in InheritanceExampleRectangle")
    }

    init {
        println("InheritanceExampleRectangle :: vertexCount : $vertexCount")
    }
}

class InheritanceSmallExample : InheritanceExampleRectangle() {
    override val vertexCount: Int
        get() = super.vertexCount

    init {
        println("You are not able to override draw function of InheritanceExampleRectangle class.")
        println("InheritanceSmallExample :: vertexCount : $vertexCount")
    }
}

class InheritanceExampleMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val smallExample = InheritanceSmallExample()
            smallExample.draw() // Accessing the draw() of base class.
            smallExample.fill() // Accessing the fill() of root base class.
            val rectangleExample = InheritanceExampleRectangle()
            rectangleExample.draw() // Accessing the draw() of its class.
            rectangleExample.fill() // Accessing the fill() of root base.
            val circleExample = InheritanceExampleCircle()
            circleExample.draw() // Accessing the draw() of its class.
            circleExample.fill() // Accessing the fill() of base class.
        }
    }
    /*
    * Output - Comments are not added in the output.
    * InheritanceExampleShape :: vertexCount : 0 // Called the init block of root base class.
    * InheritanceExampleRectangle :: vertexCount : 4 // Called the init block of base class.
    * You are not able to override draw function of InheritanceExampleRectangle class. // Called its init block
    * InheritanceSmallExample :: vertexCount : 4 // Called its init block
    * Draw function in InheritanceExampleRectangle // Called its draw()
    * Fill function in InheritanceExampleShape // Called its fill()
    * InheritanceExampleShape :: vertexCount : 0 // Called the init block of root base class.
    * InheritanceExampleRectangle :: vertexCount : 4 // Called the init block of base class.
    * Draw function in InheritanceExampleRectangle // Called its draw()
    * Fill function in InheritanceExampleShape // Called its fill()
    * InheritanceExampleShape :: vertexCount : 0 // Called the init block of root base class.
    * InheritanceExampleCircle :: vertexCount : 0 // Called the init block of base class.
    * Draw function in InheritanceExampleCircle // Called its draw()
    * Fill function in InheritanceExampleShape // Called its fill()
    * */
}

/**
 * Inheritance : Derived class initialization order
 * */
open class InitializationOrderBase(name: String) {
    init {
        println("Initialization of the base class.")
    }

    open val size = name.length.also { println("Initializing size on the base class: $it") }
}

class InitializationOrderDerived(name: String, lastName: String) :
    InitializationOrderBase(name.replaceFirstChar { it.uppercase() }
        .also { println("Argument for the base class $it") }) {
    override val size = (super.size + lastName.length).also { println("Initializing size on the derived class: $it") }

    init {
        println("Initialization of the derived class.")
    }
}

class InitializationOrderMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            InitializationOrderDerived("raj", "kanchan".replaceFirstChar { it.uppercase() })
        }
    }
    /*
    * Output
    * Argument for the base class Raj
    * Initialization of the base class.
    * Initializing size on the base class: 3
    * Initializing size on the derived class: 10
    * Initialization of the derived class.
    * */
}

/**
 * Inheritance : Calling the superclass implementation.
 * */
open class SuperImplementationRectangle {
    open fun draw() {
        println("Drawing the Rectangle")
    }

    val borderColor: String get() = "black"
}

class SuperImplementationFilledRectangle : SuperImplementationRectangle() {
    override fun draw() {
        val filler = Filler()
        filler.drawAndFill()
    }

    inner class Filler {
        private fun fill() {
            println("Filling")
        }

        fun drawAndFill() {
            fill()
            println("Drawn a filled rectangle with color $borderColor")
        }
    }
}

class SuperImplementationMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fillRectangle = SuperImplementationFilledRectangle()
            fillRectangle.draw()
        }
    }
    /*
    * Output
    * Filling
    * Drawn a filled rectangle with color black
    * */
}