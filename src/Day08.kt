fun main() {
    class InfiniteStringIterator(private val inputString: String) : Iterator<Char> {
        private var currentIndex = 0

        override fun hasNext(): Boolean {
            return true // Always hasNext since it's an infinite iterator
        }

        override fun next(): Char {
            val currentChar = inputString[currentIndex]
            currentIndex = (currentIndex + 1) % inputString.length
            return currentChar
        }
    }

    fun part1(input: List<String>): Int {
        val executeCommands = input[0]
        var steps = 0
        val map = input.drop(2).associate { entry ->
            val split = entry.split(" = ")
            val right = split[1].split(", ")
            val pair = Pair(right[0].substring(1), right[1].substring(0, right[1].length - 1))
            split[0] to pair
        }
        val infiniteIterator = InfiniteStringIterator(executeCommands)
        var currentPosition = map["AAA"]
        while (infiniteIterator.hasNext()) {
            val doYouKnowTheWay = infiniteIterator.next()
            val newKey = when (doYouKnowTheWay) {
                'R' -> currentPosition?.second
                'L' -> currentPosition?.first
                else -> "AAA"
            }
            currentPosition = map[newKey]
            steps++

            if (newKey == "ZZZ") break
        }
        return steps
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }


    fun part2(input: List<String>): Long {
        val executeCommands = input[0]
        val map = input.drop(2).associate { entry ->
            val split = entry.split(" = ")
            val right = split[1].split(", ")
            val pair = Pair(right[0].substring(1), right[1].substring(0, right[1].length - 1))
            split[0] to pair
        }
        val infiniteIterator = InfiniteStringIterator(executeCommands)
        val currentPositions = map.entries.filter { it.key.endsWith('A') }.toList().toMutableList()
        val innerStepsList = currentPositions.map { entry ->
            var temp = entry
            var innerSteps = 0
            while (infiniteIterator.hasNext()) {
                val doYouKnowTheWay = infiniteIterator.next()
                val newKey = when (doYouKnowTheWay) {
                    'R' -> temp.value.second
                    'L' -> temp.value.first
                    else -> {
                        ""
                    }
                }
                innerSteps++

                temp = map.entries.find { it.key == newKey }!!
                if (newKey.endsWith("Z")) break

            }
            innerSteps
        }
        val findLCMOfListOfNumbers = findLCMOfListOfNumbers(innerStepsList.map { it.toLong() })
        return findLCMOfListOfNumbers

    }

//
//    val testInput = readInput("Day08_test")
//    check(part1(testInput) == 2)
    val testInput2 = readInput("Day08_test")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
