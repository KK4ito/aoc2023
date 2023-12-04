import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach { line ->
            val numbers = line.split(":")[1].split("|")
            val winningNumbers = numbers[0].trim().split(" ")
            val myNumbers = numbers[1].trim().split("\\s+".toRegex())
            val commonNumbers = myNumbers.intersect(winningNumbers.toSet())

            sum += 2.0.pow(commonNumbers.size - 1).toInt()
        }
        return sum
    }
    
    fun part2(input: List<String>): Int {
        val listOfExtraRuns = MutableList(input.size) { 1 }
        input.forEachIndexed { index, line ->
            val numbers = line.split(":")[1].split("|")
            val winningNumbers = numbers[0].trim().split(" ")
            val myNumbers = numbers[1].trim().split("\\s+".toRegex())
            val commonNumbers = myNumbers.intersect(winningNumbers.toSet())
            val incForNext = commonNumbers.size
            val extraTimes = listOfExtraRuns[index]
            for (m in 1 .. extraTimes) {
                for (n in 1..incForNext) {
                    listOfExtraRuns[index + n]++ 
                }    
            }
        }
        return listOfExtraRuns.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day04_test")
    check(part2(testInput2) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
