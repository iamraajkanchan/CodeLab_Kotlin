package inheritance

class ChildInheritanceExample : ParentInheritanceExample {
    private var childName: String? = null
    private var childAge: Int? = null

    constructor(): super()

    constructor(name: String, relation: String, childName: String, childAge: Int) : super(name, relation) {
        this.childName = childName
        this.childAge = childAge
    }

}