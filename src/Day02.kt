fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        val possibleCubes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )
        var index = 1
        input.forEach { line ->
            val split = line.split(":")
            val setsOfCubes = split[1].split(";")
            var possible = true;

            setsOfCubes.forEach { set ->
                set.trim().split(", ").map { entry ->
                    val cube = entry.split(" ")
                    val cnt = cube[0].toInt()
                    val colour = cube[1]
                    val mapEntry = possibleCubes[colour]
                    if (mapEntry == null || cnt > mapEntry) {
                        possible = false
                    }
                }

            }
            if (possible) sum += index
            index++
        }
        println(sum)

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        input.forEach { line ->
            var mapOfFewestCube = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0
            )
            val split = line.split(":")
            val setsOfCubes = split[1].split(";")

            setsOfCubes.forEach { set ->
                set.trim().split(", ").map { entry ->
                    val cube = entry.split(" ")
                    val cnt = cube[0].toInt()
                    val colour = cube[1]
                    val mapEntry = mapOfFewestCube[colour]
                    if (mapEntry != null) {
                        if (mapEntry < cnt) {
                            mapOfFewestCube[colour] = cnt
                        }
                    }
                }
            }
            sum += mapOfFewestCube.values.fold(1) {sum ,element -> sum * element}
        }
        println(sum)

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
