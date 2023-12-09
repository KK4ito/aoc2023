fun main() {

    fun calculateDifferences(numbers: List<Int>): MutableList<Int> {
        if (numbers.size < 2) {
            throw IllegalArgumentException("List must have at least two numbers to calculate differences.")
        }
        return numbers.zipWithNext { a, b -> b - a }.toMutableList()
    }

    fun part1(input: List<String>): Long {
        val toListResult = input.map { line ->
            val listOfDiffs = mutableListOf<MutableList<Int>>()
            var temp = line.split(" ").map { it.toInt() }.toMutableList()
            listOfDiffs.add(temp)

            while (temp.any { it != 0}) {
                temp = calculateDifferences(temp)
                listOfDiffs.add(temp)
            }
            listOfDiffs.last().add(0)
            listOfDiffs.reversed().zipWithNext { a, b -> b.add(a.last() + b.last()) }
            listOfDiffs.first().last().toLong()
        }.toList()

        return toListResult.sum()
    }


    fun part2(input: List<String>): Long {
        val toListResult = input.map { line ->
            val listOfDiffs = mutableListOf<MutableList<Int>>()
            var temp = line.split(" ").map { it.toInt() }.toMutableList()
            listOfDiffs.add(temp)

            while (temp.any { it != 0}) {
                temp = calculateDifferences(temp)
                listOfDiffs.add(temp)
            }
            listOfDiffs.last().add(0,0)
            listOfDiffs.reversed().zipWithNext { a, b -> b.add(0, b.first() - a.first()) }
            listOfDiffs.first().first().toLong()
        }.toList()

        return toListResult.sum()
    }


    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    val testInput2 = readInput("Day09_test")
    check(part2(testInput2) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
