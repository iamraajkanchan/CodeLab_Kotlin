package classes.interfaces

open class ImplementRelation : IRelation {
    override fun formRelation(calledFrom: String) {
        println("Relation is formed for $calledFrom")
    }

    open fun breakRelation() {
        println("Please think again before breaking the relation.")
    }
}