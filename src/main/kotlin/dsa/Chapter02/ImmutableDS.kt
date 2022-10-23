class ImmutableDS {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday")
            val modifiedDays = days + "Thursday"
            println("Days - $days, Modified Days - $modifiedDays")

            val months = arrayListOf("January", "February", "March", "April")
            months.add("May")
            println("Months - $months")
        }
    }
}

/**
 * Output
 * Days - [Sunday, Monday, Tuesday, Wednesday], Modified Days - [Sunday, Monday, Tuesday, Wednesday, Thursday]
 * Months - [January, February, March, April, May]
 * */
