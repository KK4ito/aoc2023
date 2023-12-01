fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.stream().forEach { line ->
            var firstDigit = 0
            var lastDigit = 0
            var startIndex = 0
            var endIndex = line.length - 1
            for (i in line.indices) {
                val startChar = line[startIndex]
                val endChar = line[endIndex]
                if (firstDigit == 0 && Character.isDigit(startChar)) {
                    firstDigit = startChar.digitToInt()
                }
                if (lastDigit == 0 && Character.isDigit(endChar)) {
                    lastDigit = endChar.digitToInt()
                }
                if (firstDigit != 0 && lastDigit != 0) break
                startIndex++
                endIndex--
            }
            sum += "$firstDigit$lastDigit".toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val wordToNumberMap = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        var sum = 0
        input.forEach { line ->
            val firstDigitChar = line.firstOrNull { it.isDigit() }
            val lastDigitChar = line.reversed().firstOrNull { it.isDigit() }
            var firstDigit = firstDigitChar?.digitToInt()
            var lastDigit = lastDigitChar?.digitToInt()
            var firstIndex = firstDigitChar?.let { line.indexOf(it) } ?: Int.MAX_VALUE
            var lastIndex = lastDigitChar?.let { line.lastIndexOf(it) } ?: Int.MIN_VALUE
            wordToNumberMap.forEach { entry ->
                val fIndex = line.indexOf(entry.key)
                val lIndex = line.lastIndexOf(entry.key)
                if (fIndex != -1) {
                    if (fIndex < firstIndex) {
                        firstDigit = entry.value
                        firstIndex = fIndex
                    }
                }
                if (lIndex != -1) {
                    if (lIndex > lastIndex) {
                        lastDigit = entry.value
                        lastIndex = lIndex
                    }
                }
            }
            sum += "$firstDigit$lastDigit".toInt()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
