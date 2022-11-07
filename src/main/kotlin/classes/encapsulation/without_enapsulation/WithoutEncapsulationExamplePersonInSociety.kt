package classes.encapsulation.without_enapsulation

class WithoutEncapsulationExamplePersonInSociety {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val christine = WithoutEncapsulationExamplePersonWithRelation()
            christine.fatherName = "Jack Parser"
            christine.motherName = "Katy Perry"
            christine.brotherName = "Tom Holland"
            christine.sisterName = "Marry Jane"
            christine.partnerName = "Mark Robinson"
        }
    }
}