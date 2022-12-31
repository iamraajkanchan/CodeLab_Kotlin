package functions

import kotlinx.coroutines.*

enum class CalculationType {
    ADD, MULTIPLY, MINUS, DIVIDE
}

class IntroductionToLambda {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            display("Hello World") { displayClassName() }
            runBlocking {
                launch {
                    delay(1000L)
                    display("Would you like to proceed") { displayThreadName() }
                }
            }
            calculate(CalculationType.ADD, 45, 209) { command, a, b -> executeCalculation(command, a, b) }
            calculate(CalculationType.MULTIPLY, 234, 281) { command, a, b -> executeCalculation(command, a, b) }
            calculate(CalculationType.MINUS, 2398, 1912) { command, a, b -> executeCalculation(command, a, b) }
            calculate(CalculationType.DIVIDE, 9398, 392) { command, a, b -> executeCalculation(command, a, b) }
        }

        private fun display(text: String, function: () -> Unit) {
            println(text)
            function.invoke()
        }

        private fun displayClassName() {
            val introductionToLambda = IntroductionToLambda()
            println("Class Name is ${introductionToLambda.javaClass.canonicalName}")
        }

        private fun displayThreadName() {
            println("You are working inside Coroutine Scope")
        }

        private fun calculate(
            operation: CalculationType, firstInput: Int, secondInput: Int,
            calculate: (operation: CalculationType, a: Int, b: Int) -> Int
        ) {
            println(
                "The $operation of $firstInput and $secondInput: ${
                    calculate.invoke(operation, firstInput, secondInput)
                }"
            )
        }

        private fun executeCalculation(command: CalculationType, a: Int, b: Int): Int {
            val result: Int = when (command) {
                CalculationType.ADD -> a + b
                CalculationType.MULTIPLY -> a * b
                CalculationType.MINUS -> a - b
                CalculationType.DIVIDE -> a / b
            }
            return result
        }
    }
}