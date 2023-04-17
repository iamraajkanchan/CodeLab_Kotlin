package constructors

class ConstructorsExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val cc = CustomConstructorsExample("Raj Kanchan")
            val cc2 = CustomConstructorsExample(12)
        }
    }
}

class CustomConstructorsExample {

    private var className: String? = null
    private var classNumber: Int? = null

    constructor(className: String) {
        this.className = className
        displayClassName()
    }

    constructor(classNumber: Int) {
        this.classNumber = classNumber
        displayClassNumber()
    }

    private fun displayClassName() {
        println("Class Name : ${this.className}")
    }

    private fun displayClassNumber() {
        println("Class Number: ${this.classNumber}")
    }

}