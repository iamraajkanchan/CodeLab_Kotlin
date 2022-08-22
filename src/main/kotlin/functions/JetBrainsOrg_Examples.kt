package functions

/**
 * Functions - Default arguments
 * */
class FunctionWithDefaultArguments {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            printValues(0) {
                println("Testing the bar argument")
            }
            printValues(1) {
                println("Testing the baz argument")
            }
            printValues(qux = {
                println("Testing the qux argument")
            })
        }

        /* Passing an anonymous function (lambda function) into the argument. */
        private fun printValues(bar: Int = 0, baz: Int = 1, qux: () -> Unit) {
            println("FunctionWithDefaultArguments :: printValues :: bar : $bar")
            println("FunctionWithDefaultArguments :: printValues :: baz : $baz")
            qux.invoke() // Use the invoke method
        }
    }
    /*
    * Output
    * FunctionWithDefaultArguments :: printValues :: bar : 0
    * FunctionWithDefaultArguments :: printValues :: baz : 1
    * Testing the bar argument
    * FunctionWithDefaultArguments :: printValues :: bar : 1
    * FunctionWithDefaultArguments :: printValues :: baz : 1
    * Testing the baz argument
    * FunctionWithDefaultArguments :: printValues :: bar : 0
    * FunctionWithDefaultArguments :: printValues :: baz : 1
    * Testing the qux argument
    * */
}