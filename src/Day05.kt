fun main() {

    class RangeStartingPoint(iter: ListIterator<List<Long>>) {
        val intRanges = mutableMapOf<LongRange, Long>()

        init {
            iter.forEach { subEntry ->
                intRanges[subEntry[1]..<subEntry[1] + subEntry[2]] = subEntry[0] - subEntry[1]
            }
        }

        fun getNumberIfInRange(inputNumber: Long): Long {
            val possibleMatch = intRanges.entries.find { entry -> inputNumber in entry.key }?.value
            if (possibleMatch != null) return inputNumber + possibleMatch
            return inputNumber
        }
    }

    fun constructMapping(input: List<String>): MutableList<List<List<Long>>> {
        val maps: MutableList<List<List<Long>>> = mutableListOf()
        var currentMap: MutableList<List<Long>>? = null
        for (line in input.drop(2)) { // Skip the first two lines
            if (line.isBlank()) continue
            if (line.endsWith("map:")) {
                currentMap = mutableListOf()
                maps.add(currentMap)
            } else {
                currentMap?.add(line.split(" ").map { it.toLong() })
            }
        }
        return maps
    }

    fun processInput(
        maps: MutableList<List<List<Long>>>,
        seedsLine: List<Long>
    ): List<Long> {
        val toList = maps.map { entry -> RangeStartingPoint(entry.listIterator()) }.toList()
        val locationNumber = seedsLine.map { seed ->
            var processNumber = seed
            toList.map { entry -> processNumber = entry.getNumberIfInRange(processNumber) }
            processNumber
        }.toList()
        return locationNumber
    }

    fun processInputParellel(
        maps: MutableList<List<List<Long>>>,
        seedsLongRangeLine: List<LongRange>
    ): Long {
        val toList = maps.map { entry -> RangeStartingPoint(entry.listIterator()) }.toList()
        val longList = seedsLongRangeLine.map { seedsLine ->
            var minLocationNumber = Long.MAX_VALUE
            seedsLine.forEach { seed ->
                var processNumber = seed
                toList.forEach { entry -> processNumber = entry.getNumberIfInRange(processNumber) }
                if (processNumber < minLocationNumber){
                    minLocationNumber = processNumber
                }
            }
            minLocationNumber
        }
        return longList.min()
    }

    fun part1(input: List<String>): Long {
        val seedsLine = input.first().split(":")[1].trim().split(" ").map { it.toLong() }
        val locationNumber = processInput(constructMapping(input), seedsLine)
        return locationNumber.min()
    }

    fun part2(input: List<String>): Long {
        val split = input.first().split(":")[1].trim().split(" ").map { it.toLong() }
        val pairedRanges = split.chunked(2).map { pair ->
            pair[0]..<pair[0] + pair[1]
        }.toList()
//        val seedsLine = pairedRanges.flatMap { it.toList() } // hello fucking no
        val locationNumber = processInputParellel(constructMapping(input), pairedRanges)

        return locationNumber
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    val testInput2 = readInput("Day05_test")
    check(part2(testInput2) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
