package classes.encapsulation.without_enapsulation

class WithoutEncapsulationExamplePersonInAcademics {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val christine = WithoutEncapsulationExamplePersonWithRelation()
            christine.fatherName = "Jack Parser"
            christine.motherName = "Marry Jane"
            christine.brotherName = "Tom Holland"
            christine.sisterName = "Katy Perry"
            christine.partnerName = "Mark Robinson"
        }
    }
}