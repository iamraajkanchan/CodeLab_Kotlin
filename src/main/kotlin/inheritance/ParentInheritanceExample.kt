package inheritance

open class ParentInheritanceExample constructor() {
    private var parentName: String? = null
    private var relation: String? = null

    constructor(parentName: String, relation: String) : this() {
        this.parentName = parentName;
        this.relation = relation;
    }
}