package leetcode

import java.text.SimpleDateFormat
import java.util.*

class Problem1 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            twoSum(intArrayOf(2, 7, 11, 15), 9).forEach {
                print(it)
            }
            val currentDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH)
            val currentDateString = currentDateFormat.format(Date())
            println(currentDateString)
        }

        private fun twoSum(numbers: IntArray, target: Int): IntArray {
            val indexArray = mutableListOf<Int>()
            var sum = 0
            numbers.forEachIndexed { index, number ->
                sum += number
                if (sum == target) {
                    indexArray.add(index)
                }
            }
            return indexArray.toIntArray()
        }

        private fun getTodayList() {
            val currentDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val referenceDate = currentDateFormat.format(Date())

        }

        private fun getYesterdayList() {

        }
    }
}