package classes.encapsulation.without_enapsulation

class WithoutEncapsulationExamplePersonInOffice {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val christine = WithoutEncapsulationExamplePersonWithRelation()
            christine.fatherName = "Mark Robinson"
            christine.motherName = "Marry Jane"
            christine.brotherName = "Tom Holland"
            christine.sisterName = "Katy Perry"
            christine.partnerName = "Jack Parser"
        }
    }
}