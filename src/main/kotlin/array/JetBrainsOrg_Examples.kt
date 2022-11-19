package array

import java.text.SimpleDateFormat
import java.util.*

class FindDateWithArray {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val leaveDates = arrayOf(
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022",
                "17-11-2022"
            )
            val calendar = Calendar.getInstance()
            val currentDateFormat = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = currentDateFormat.format(calendar.time)
            if (leaveDates.contains(currentDate)) {
                println("You have already applied for leave!!!")
            } else {
                println("You can apply for leave today")
            }
        }
    }
}