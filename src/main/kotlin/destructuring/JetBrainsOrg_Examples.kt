package destructuring

/**
 * Destructuring Declarations - Basic Destructuring
 * Using Object of a Class and componentN method of Object.
 * */
class BasicDestructuring {
    data class Teacher(val name: String, val age: Int)
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val historyTeacher = Teacher("Claire Mathias", 24)
            /* To de-structure the properties of a class ma*/
            val (historyTeacherName, historyTeacherAge) = historyTeacher
            println("Name of the History teacher is $historyTeacherName, who is just $historyTeacherAge year old.")
            val civicsTeacher = Teacher("James Chad", 28)
            val civicsTeacherName = civicsTeacher.component1()
            val civicsTeacherAge = civicsTeacher.component2()
            println("Name of the Civics teacher is $civicsTeacherName, who is just $civicsTeacherAge year old.")
        }
    }
    /*
    * Output
    * Name of the History teacher is Claire Mathias, who is just 24 year old.
    * Name of the Civics teacher is James Chad, who is just 28 year old.
    * */
}