package classes.encapsulation.without_enapsulation

class WithoutEncapsulationExamplePersonInOffice {
    var christine: WithoutEncapsulationExamplePersonWithRelation? = null
    companion object {
        private var personInOffice : WithoutEncapsulationExamplePersonInOffice? = null
        @JvmStatic
        fun main(args: Array<String>) {
            personInOffice = WithoutEncapsulationExamplePersonInOffice()
            personInOffice?.christine = WithoutEncapsulationExamplePersonWithRelation().apply {
                fatherName = "Mark Robinson"
                motherName = "Marry Jane"
                brotherName = "Tom Holland"
                sisterName = "Katy Perry"
                partnerName = "Jack Parser"
            }
            getInstance(personInOffice)
        }
        fun getInstance(personInOffice: WithoutEncapsulationExamplePersonInOffice?) : WithoutEncapsulationExamplePersonInOffice? {
            return personInOffice
        }
    }
}