package classes

import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator
import javax.xml.crypto.KeySelector.Purpose
import kotlin.random.Random

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
open class InheritanceExampleShape(open val color: String) {

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

class InheritanceExampleCircle(private val circleColor: String) : InheritanceExampleShape(circleColor) {
    override var vertexCount: Int = 0
    override fun draw() {
        println("Draw function in InheritanceExampleCircle")
    }

    init {
        println("InheritanceExampleCircle :: vertexCount : $vertexCount")
    }
}

open class InheritanceExampleRectangle(private val rectangleColor: String) : InheritanceExampleShape(rectangleColor) {
    override val vertexCount: Int = 4
    final override fun draw() {
        println("Draw function in InheritanceExampleRectangle")
    }

    init {
        println("InheritanceExampleRectangle :: vertexCount : $vertexCount")
    }
}

class InheritanceSmallExample(color: String) : InheritanceExampleRectangle(color) {
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
            val smallExample = InheritanceSmallExample("Yellow")
            smallExample.draw() // Accessing the draw() of base class.
            smallExample.fill() // Accessing the fill() of root base class.
            val rectangleExample = InheritanceExampleRectangle("Blue")
            rectangleExample.draw() // Accessing the draw() of its class.
            rectangleExample.fill() // Accessing the fill() of root base.
            val circleExample = InheritanceExampleCircle("Green")
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

/**
 * Inheritance - Overriding Rules - Accessing parent methods with the help of super keyword.
 * */
open class OverridingRulesExampleRectangle {
    open fun draw() {
        println("Drawing in OverridingRulesExampleRectangle class")
    }
}

/* Note: Same name of a class and interface is not invalid */
interface OverridingRulesExamplePolygon {
    fun draw() {
        println("Drawing in OverridingRulesExamplePolygon interface")
    }
}

class OverridingRulesExampleSquare : OverridingRulesExampleRectangle(), OverridingRulesExamplePolygon {
    override fun draw() {
        println("Drawing implemented from OverridingRulesExamplePolygon and called in OverridingRulesExampleSquare")
        /* Use such syntax to call methods of a base class */
        super<OverridingRulesExampleRectangle>.draw()
        /* Use such syntax to call methods of an interface class */
        super<OverridingRulesExamplePolygon>.draw()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val square = OverridingRulesExampleSquare()
            square.draw()
        }
    }
    /*
    * Output
    * Drawing implemented from OverridingRulesExamplePolygon and called in OverridingRulesExampleSquare
    * Drawing in OverridingRulesExampleRectangle class
    * Drawing in OverridingRulesExamplePolygon interface
    * */
}

/**
 * Properties - Getters And Setters
 * */

/* Here width and height is a property of GetterAndSetterRectangleExample class*/
class GetterAndSetterRectangleExample(private val width: Int, private val height: Int) {
    val area: Int
        get() = this.width * this.height
    var nameOfShape: String = ""
        get() = field
        set(value) {
            field = value.replaceFirstChar { it.uppercase() }
        }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val rectangle = GetterAndSetterRectangleExample(300, 600)
            rectangle.nameOfShape = "my flat"
            println("Area of ${rectangle.nameOfShape} with ${rectangle.width} width and ${rectangle.height} height is ${rectangle.area}")
        }
    }
    /*
    * Output
    * Area of My flat area with 300 width and 600 height is 180000
    * */
}

/**
 * Properties - Getters and Setters - Backing Field
 * */
class BackingFieldExample {
    private var counter = 0
        set(value) {
            field = if (value > 0) {
                value
            } else {
                0
            }
        }

    private val isValidCounter: Boolean
        get() = this.counter != 0

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val example = BackingFieldExample()
            example.counter = readln().toInt()
            if (example.isValidCounter) {
                println("The value of counter in BackingFieldExample class is ${example.counter}")
            } else {
                println("The value of counter in BackingFieldExample class is not valid")
            }
        }
    }
    /*
    * Output
    * The value of counter in BackingFieldExample class is 25
    * */
}

/**
 * Properties - Getters and Setters  - Backing Properties
 * */
class BackingPropertiesExample {

    /* The property _table is editable, but it is not accessible outside the class.
    Can be used for internal purpose only */
    /* So you have to make a property that is editable inside a class,
    but it must not be accessible outside the class */
    private var _table: Map<String, Int>? = null

    /* Though the property table is accessible is outside the class, but it is not editable */
    /* So you have to make a property that is accessible outside the class, but it must not be editable. */
    val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap()
            }
            return _table ?: throw AssertionError("Set to null by another thread")
        }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val example = BackingPropertiesExample()
            example._table = buildMap {
                this["firstKey"] = 1
                this["secondKey"] = 2
                this["thirdKey"] = 3
                this["fourthKey"] = 4
            }
            for (key in example.table.keys) {
                println("Table : [Key - $key, Value - ${example.table[key]}]")
            }
        }
    }
}

/**
 * Properties - Late Initialized Properties and Variables - lateinit
 * */
class LateInitExampleSubject {
    fun method() {
        println("Method function of LateInitExampleSubject")
    }
}

class LateInitExampleMain {
    // lateinit var counter: Int // lateinit can't be used for Primitive Types
    lateinit var subject: LateInitExampleSubject

    private fun setup() {
        subject = LateInitExampleSubject()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = LateInitExampleMain()
            main.setup()
            main.subject.method()
        }
    }
    /*
    * Output
    * Method function of LateInitExampleSubject
    * */
}

/**
 * Inheritance - Properties in Interfaces
 * */

interface InterfaceForInterfaceWithProperties {
    val prop: Int
    val propertyWithImplementation: Int
        get() = 0

    /* To execute this method you have to create an instance of the Interface */
    fun displayProperty() {
        println("Property without implementation : $prop")
        println("Property with implementation: $propertyWithImplementation")
    }
}

/*
* When you are implementing an interface you won't have to call its constructor i.e. ()
* But when you are extending a class you must have to call its primary constructor i.e. ()
*/
class InterfaceWithPropertiesMain : InterfaceForInterfaceWithProperties {
    /* If you don't initialize the property of an interface in its body then you have
    to initialize the property in the implementing class*/
    override val prop: Int
        get() = 1

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = InterfaceWithPropertiesMain()
            /*
            * Because the class implements InterfaceForInterfaceWithProperties that's why you can call the
            * method of the interface directly.
            * */
            main.displayProperty()
        }
    }
    /*
    * Output
    * Property without implementation : 1
    * Property with implementation: 0
    * */
}

/**
 * Interface - Interface Inheritance
 * */

interface InheritanceOnInterfaceNamed {
    val name: String
}

interface InheritanceOnInterfacePerson : InheritanceOnInterfaceNamed {
    val firstName: String
    val lastName: String
    override val name: String
        get() = "$firstName $lastName"
}

class InheritanceOnInterfaceEmployee(override val firstName: String, override val lastName: String) :
    InheritanceOnInterfacePerson {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val employee = InheritanceOnInterfaceEmployee("Katy", "Perry")
            println("Name of Employee: ${employee.name}")
        }
    }
    /*
    * Output
    * Name of Employee: Katy Perry
    * */
}

/**
 * Inheritance - Resolving overriding conflicts
 * */

interface ResolvingOverridingA {
    fun foo() {
        println("ResolvingOverridingA :: foo")
    }

    fun bar()
}

interface ResolvingOverridingB {
    fun foo() {
        println("ResolvingOverridingB :: foo")
    }

    fun bar() {
        println("ResolvingOverridingB :: bar")
    }
}

class ResolvingOverridingClass : ResolvingOverridingA {
    override fun bar() {
        println("ResolvingOverridingA :: ResolvingOverridingClass :: bar")
    }
}

class ResolvingOverridingMain : ResolvingOverridingA, ResolvingOverridingB {
    override fun foo() {
        super<ResolvingOverridingA>.foo()
        super<ResolvingOverridingB>.foo()
    }

    override fun bar() {
        super<ResolvingOverridingB>.bar()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = ResolvingOverridingMain()
            main.foo()
            main.bar()
            val classObject = ResolvingOverridingClass()
            classObject.bar()
        }
    }
    /*
    * Output
    * ResolvingOverridingA :: foo
    * ResolvingOverridingB :: foo
    * ResolvingOverridingB :: bar
    * ResolvingOverridingA :: ResolvingOverridingClass :: bar
    * */
}

/**
 * Functional (SAM) Interfaces - SAM Conversions
 * */

class SAMConversionsExampleMain {
    fun interface IntPredicate {
        fun accept(number: Int): Boolean
    }

    companion object {
        /* If you are using Functional Interface, you don't have to use object keyword to create its instance. */
        private val isEven = IntPredicate { it % 2 == 0 }

        @JvmStatic
        fun main(args: Array<String>) {
            val number = readln().toInt()
            println("Is $number even? ${isEven.accept(number)}")
        }
    }
    /*
    * Output
    * Is 2909031 even? false
    * */
}

/**
 * Extensions - Extension Functions
 * */
class ExtensionFunctionsExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val list = mutableListOf('a', 'e', 'i', 'o', 'u')
            println("Before Swap: $list")
            list.swap(2, 3)
            println("After Swap: $list")
        }

        private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
            val temp = this[index1] // This corresponds to the list
            this[index1] = this[index2]
            this[index2] = temp
        }
    }
    /*
    * Output
    * Before Swap: [a, e, i, o, u]
    * After Swap: [a, e, o, i, u]
    * */
}

/**
 * Extension Functions - Extensions are resolved statically
 * */
class StaticBehaviorOfExtensionFunctions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            /* Defining a base class inside a function */
            open class Shape

            /* Defining a derived class inside a function */
            class Rectangle : Shape() {
                fun printFunctionType() = println("Class Method")
            }

            fun Shape.getName() = println("Shape")
            fun Rectangle.getName() = println("Rectangle")

            fun printClassName(shape: Shape) {
                shape.getName()
            }
            printClassName(Rectangle()) // Output Shape

            fun Rectangle.printFunctionType() = println("Extension Function")
            Rectangle().printFunctionType()

            fun Rectangle.printFunctionType(i: Int) = println("Extension Function with parameter: $i")
            Rectangle().printFunctionType(45)

        }
    }
    /*
    * Output
    * Shape
    * Class Method
    * Extension Function with parameter: 45
    * */
}

/**
 * Extension Functions - Nullable Receiver
 * */
class NullableReceiverForExtensionFunctions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var main: NullableReceiverForExtensionFunctions? = null
            println("Before assigning object: ${main.getClassName()}")
            main = NullableReceiverForExtensionFunctions()
            println("After assigning object: ${main.getClassName()}")
        }

        private fun NullableReceiverForExtensionFunctions?.getClassName(): String {
            if (this == null) return "null"
            return "Nullable Receiver Class For Extension Functions"
        }
    }
    /*
    * Output
    * Before assigning object: null
    * After assigning object: Nullable Receiver Class For Extension Functions
    * */
}

/**
 * Extension Functions - Companion object extensions
 * */
class ExtensionFunctionsInCompanionObjects {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ExtensionFunctionsInCompanionObjects.printCompanion()
        }

        private fun ExtensionFunctionsInCompanionObjects.Companion.printCompanion() = println("Companion")
    }
    /*
    * Output
    * Companion
    * */
}

/**
 * Extension Functions : Declaring extensions as members
 * */

class ExtensionFunctionsWithMembersHost(private val hostName: String) {
    fun printHostName() {
        print(hostName)
    }
}

class ExtensionFunctionsWithMembersConnection(
    private val host: ExtensionFunctionsWithMembersHost, private val portName: String
) {

    private fun printPort() {
        print(portName)
    }

    private fun ExtensionFunctionsWithMembersHost.printConnectionString() {
        printHostName()
        print(":")
        printPort()
    }

    private fun ExtensionFunctionsWithMembersHost.getConnectionString() {
        println()
        println(toString()) // Calls ExtensionFunctionsWithMembersHost.toString()
        println(this@ExtensionFunctionsWithMembersConnection.toString()) // Calls ExtensionFunctionsWithMembersConnection.toString()
    }

    fun connect() {
        host.printConnectionString() // Calls the extension function
        host.getConnectionString()
    }
}

class ExtensionFunctionsWithMembers {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ExtensionFunctionsWithMembersConnection(ExtensionFunctionsWithMembersHost("google.com"), "443").connect()
        }
    }
    /*
    * Output
    * google.com:443
    * classes.ExtensionFunctionsWithMembersHost@45ff54e6
    * classes.ExtensionFunctionsWithMembersConnection@2328c243
    * */
}

/**
 * Extension Functions - Declaring extensions as members - Inheritance
 * */
open class ExtensionFunctionsBase {}
class ExtensionFunctionsDerived : ExtensionFunctionsBase() {}

open class ExtensionFunctionsBaseCaller() {
    open fun ExtensionFunctionsBase.printFunctionInfo() {
        println("ExtensionFunctionsBase extension function in ExtensionFunctionsBaseCaller")
    }

    open fun ExtensionFunctionsDerived.printFunctionInfo() {
        println("ExtensionFunctionsDerived extension function in ExtensionFunctionsBaseCaller ")
    }

    fun call(base: ExtensionFunctionsBase) {
        base.printFunctionInfo()
    }
}

class ExtensionFunctionsDerivedCaller : ExtensionFunctionsBaseCaller() {
    override fun ExtensionFunctionsBase.printFunctionInfo() {
        println("ExtensionFunctionsBase extension function in ExtensionFunctionsDerivedCaller")
    }

    override fun ExtensionFunctionsDerived.printFunctionInfo() {
        println("ExtensionFunctionsDerived extension function in ExtensionFunctionsDerivedCaller")
    }
}

class ExtensionFunctionsWithMembersMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ExtensionFunctionsBaseCaller().call(ExtensionFunctionsBase())
            ExtensionFunctionsDerivedCaller().call(ExtensionFunctionsBase())
            ExtensionFunctionsDerivedCaller().call(ExtensionFunctionsDerived())
        }
    }
    /*
    * Output
    * ExtensionFunctionsBase extension function in ExtensionFunctionsBaseCaller
    * ExtensionFunctionsBase extension function in ExtensionFunctionsDerivedCaller
    * ExtensionFunctionsBase extension function in ExtensionFunctionsDerivedCaller
    * */
}

/**
 * Enum classes -  Working with enum classes.
 * */

/* If you want to use the properties of enum objects declare the properties as val. */
enum class Transactions(val requestType: String, val purposeType: Int) {
    BalanceEnquiry("BalEnq", 21), CashWithdrawal("Collector", 23), CashDeposit(
        "Debit", 33
    ),
    MiniStatement("MiniStatement", 19), FundTransfer("Debit", 36),
}

class EnumClassExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(getRequestType(Transactions.CashDeposit))
            println(getPurposeType(Transactions.CashWithdrawal))
            println(getRequestType(Transactions.MiniStatement))
            println(getPurposeType(Transactions.MiniStatement))
            /* To print the values of every object of an enum class */
            println("Printing Value of Every Object of An Enum Class")
            repeat(100) { print("=") }
            println(enumValues<Transactions>().joinToString { "\nName: ${it.name} - Ordinal: ${it.ordinal} - Request Type: ${it.requestType} - Purpose Type: ${it.purposeType}" })
            /* To print the values of an individual object of an enum class */
            repeat(100) { print("=") }
            println()
            println("Printing Value of An Individual Object Of An Enum Class")
            repeat(100) { print("=") }
            println()
            println(
                "Name: ${enumValueOf<Transactions>(Transactions.FundTransfer.name).name} - Ordinal: ${
                    enumValueOf<Transactions>(
                        Transactions.FundTransfer.name
                    ).ordinal
                } - Request Type: ${enumValueOf<Transactions>(Transactions.FundTransfer.name).requestType} - Purpose Type: ${
                    enumValueOf<Transactions>(
                        Transactions.FundTransfer.name
                    ).purposeType
                }"
            )
        }

        private fun getRequestType(transactions: Transactions): String {
            return when (transactions) {
                Transactions.CashDeposit -> transactions.requestType
                Transactions.BalanceEnquiry -> transactions.requestType
                Transactions.FundTransfer -> transactions.requestType
                Transactions.MiniStatement -> transactions.requestType
                Transactions.CashWithdrawal -> transactions.requestType
            }
        }

        private fun getPurposeType(transactions: Transactions): Int {
            return when (transactions) {
                Transactions.CashDeposit -> transactions.purposeType
                Transactions.BalanceEnquiry -> transactions.purposeType
                Transactions.FundTransfer -> transactions.purposeType
                Transactions.MiniStatement -> transactions.ordinal
                Transactions.CashWithdrawal -> transactions.purposeType
            }
        }
    }
    /*
    * Output
    * Debit
    * 23
    * MiniStatement
    * 3
    * Printing Value of Every Object of An Enum Class
    * ====================================================================================================
    * Name: BalanceEnquiry - Ordinal: 0 - Request Type: BalEnq - Purpose Type: 21,
    * Name: CashWithdrawal - Ordinal: 1 - Request Type: Collector - Purpose Type: 23,
    * Name: CashDeposit - Ordinal: 2 - Request Type: Debit - Purpose Type: 33,
    * Name: MiniStatement - Ordinal: 3 - Request Type: MiniStatement - Purpose Type: 19,
    * Name: FundTransfer - Ordinal: 4 - Request Type: Debit - Purpose Type: 36
    * ====================================================================================================
    * Printing Value of An Individual Object Of An Enum Class
    * ====================================================================================================
    * Name: FundTransfer - Ordinal: 4 - Request Type: Debit - Purpose Type: 36
    * */
}

/**
 * Enum Classes - Anonymous classes
 * */
enum class OfferState(private val exemptionPercentage: Int) {
    DISCOUNT(20) {
        override fun initiateOrder(price: Int, item: String) {
            calculateAndDisplay(price, item)
        }
    },
    VOUCHER(15) {
        override fun initiateOrder(price: Int, item: String) {
            calculateAndDisplay(price, item)
        }
    };

    abstract fun initiateOrder(price: Int, item: String)
    fun calculateAndDisplay(price: Int, item: String) {
        val actualAmount = price - price * exemptionPercentage / 100
        println("You have to pay only $actualAmount for $item at $exemptionPercentage% using ${this.name}")
    }
}

class EnumWithAnonymousClassesMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            OfferState.DISCOUNT.initiateOrder(4000, "Washing Machine")
            OfferState.VOUCHER.initiateOrder(4000, "Refrigerator")
        }
    }
    /*
    * Output
    * You have to pay only 3200 for Washing Machine at 20% using DISCOUNT
    * You have to pay only 3400 for Refrigerator at 15% using VOUCHER
    * */
}

/**
 * Enum classes - Implementing interfaces with enum classes
 * */
enum class IntArithmetics : BinaryOperator<Int>, IntBinaryOperator {

    PLUS {
        override fun apply(t: Int, u: Int): Int = t + u
    },
    TIMES {
        override fun apply(t: Int, u: Int): Int = t * u
    };

    override fun applyAsInt(left: Int, right: Int): Int = apply(left, right)
}

class EnumWithInterfaces {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("PLUS operation overriding apply method ${IntArithmetics.PLUS.apply(4, 5)}")
            println("TIMES operation overriding apply method ${IntArithmetics.TIMES.apply(4, 5)}")
            println("PLUS operation overriding applyShift method ${IntArithmetics.PLUS.applyAsInt(4, 5)}")
            println("TIMES operation overriding applyShift method ${IntArithmetics.TIMES.applyAsInt(4, 5)}")
        }
    }
    /*
    * Output
    * PLUS operation overriding apply method 9
    * TIMES operation overriding apply method 20
    * PLUS operation overriding applyShift method 9
    * TIMES operation overriding applyShift method 20
    * */
}

/**
 * Inline Classes - Members
 * It is mandatory to use @JvmInline annotation when you are using Inline classes
 * Note: use 'value' modifier to declare an inline class
 * An inline class must have at least one and only one property.
 *
 * Benefits
 * If you use a normal wrapper class, you could introduce performance overheads as
 * you would be allocating memory to create the wrapper class. Whereas with inline
 * classes, there is no additional memory used since the properties are unwrapped.
 *
 * Inline classes ensures compile-time safety.
 *
 * Difference between typeAlias and inline class
 * typeAlias doesn't enforce compile time safety but inline class does.
 *
 * Inline classes are experimental. May or may not exist in Kotlin.
 *
 * */

@JvmInline
value class InlineClassWithMembersName(private val s: String) {
    init {
        require(s.isNotEmpty())
    }

    val length: Int
        get() = s.length

    fun greet() {
        println("Hello $s, welcome to the Tree House!!!")
    }
}

class InlineClassWithMembers {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val name = InlineClassWithMembersName("Amber Heard")
            name.greet()
            println("Length of name variable is ${name.length}")
        }
    }
    /*
    * Output
    * Hello Amber Heard, welcome to the Tree House!!!
    * Length of name variable is 11
    * */
}

/**
 * Inline Classes - Inheritance
 * */

/* You must use @JvmInline annotation to declare an inline class */
/* Use 'value' modifier to declare an inline class */
/* An inline class must have at least on property */

/* You can use this interface to print value from an inline class */
interface Printable {
    fun returnValue(): String
}

@JvmInline
value class InlineClassesWithInheritanceName(private val s: String) : Printable {
    init {
        require(s.isNotEmpty())
    }

    override fun returnValue(): String = "Input value is $s"

}

class InlineClassesWithInheritanceMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val name = InlineClassesWithInheritanceName(readln())
            println(name.returnValue())
        }
    }
    /*
    * Output
    * Hello World!!!
    * */
}

/**
 * Inline Classes - Medium Example - Example of using a Data Class without Inline Class.
 * @author https://medium.com/@riggaroo/lessons-learnt-with-kotlin-using-inline-classes-9c3d424650d1
 * */
data class WithOutInlineClassOrder(
    private val orderId: Int,
    private val price: Int,
    private val supplier: String,
    private val customer: String
)

class WithoutInlineClassMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            /* Here orderId is shuffled with price and supplier is shuffled with customer
            * so there is no way to check if proper parameter is added in the constructor
            * */
            val amazonOrder = WithOutInlineClassOrder(101, 298082, "Taylor Swift", "Amazon")
            println(amazonOrder.toString())
        }
    }
    /*
    * Output
    * WithOutInlineClassOrder(orderId=101, price=298082, supplier=Taylor Swift, customer=Amazon)
    * */
}

/**
 * Inline Classes - Medium Example - Example of using a Data Class with Inline Class.
 * @author https://medium.com/@riggaroo/lessons-learnt-with-kotlin-using-inline-classes-9c3d424650d1
 * */
@JvmInline
value class WithInlineClassOrderId(private val orderId: Int)

@JvmInline
value class WithInlineClassPrice(private val price: Int)

@JvmInline
value class WithInlineClassSupplier(private val supplierName: String)

@JvmInline
value class WithInlineClassCustomer(private val customerName: String)
data class WithInlineClassOrder(
    private val orderId: WithInlineClassOrderId,
    private val price: WithInlineClassPrice,
    private val supplier: WithInlineClassSupplier,
    private val customer: WithInlineClassCustomer
)

class WithInlineClassMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val amazonOrder = WithInlineClassOrder(
                WithInlineClassOrderId(298082),
                WithInlineClassPrice(101),
                WithInlineClassSupplier("Amazon"),
                WithInlineClassCustomer("Taylor Swift")
            )
            println(amazonOrder.toString())
        }
        /*
        * Output
        * WithInlineClassOrder(orderId=WithInlineClassOrderId(orderId=298082), price=WithInlineClassPrice(price=101), supplier=WithInlineClassSupplier(supplierName=Amazon), customer=WithInlineClassCustomer(customerName=Taylor Swift))
        * */
    }
}