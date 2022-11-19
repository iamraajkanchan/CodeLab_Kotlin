package regex

class CheckRegexForAddress {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(convertAddress(readln()))
        }

        /** $ sign is not working */
        private fun convertAddress(src: String?): String {
            val pattern = Regex("^[-+.,\\da-zA-Z\\s]+\$")
            val inversePattern = Regex("[^[-+.,\\da-zA-Z\\s]]")
            return if (src != null && !src.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                if (src.contains(pattern)) {
                    src.also {
                        println("Valid")
                    }
                } else {
                    src.replace(inversePattern, "").also { println("Invalid") }
                }
            } else {
                "-"
            }
        }
    }
}