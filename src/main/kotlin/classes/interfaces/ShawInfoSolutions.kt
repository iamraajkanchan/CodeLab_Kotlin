package classes.interfaces

class ShawInfoSolutions : ImplementRelation() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val shawInfoSolutions = ShawInfoSolutions()
            val implementRelation = ImplementRelation()
            println("Instance of ShawInfoSolutions is created $shawInfoSolutions")
            shawInfoSolutions.formRelation(shawInfoSolutions.javaClass.simpleName)
            println("Instance of ImplementRelation is created $implementRelation")
            implementRelation.formRelation(implementRelation.javaClass.simpleName)
        }
    }
}