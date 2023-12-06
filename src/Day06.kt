fun main() {


    fun part1(input: List<String>): Int {
        val timeNumbers = input[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toInt() }
        val distanceNumbers = input[1].split(":")[1].trim().split("\\s+".toRegex()).map { it.toInt() }

        val toList = timeNumbers.mapIndexed() { index, time ->
            var cntRecordBroken = 0
            for (i in 1..time) {
                val holdDuration = i
                val driveDuration = time - i
                val distanceDone = holdDuration * driveDuration
                if (distanceDone > distanceNumbers[index]) cntRecordBroken++
            }
            cntRecordBroken
        }.toList()
        return toList.reduce { sum, value -> sum * value }

    }

    fun part2(input: List<String>): Long {
        val time = input[0].split(":")[1].replace(" ", "").toLong()
        val distance = input[1].split(":")[1].replace(" ", "").toLong()

        var lowest = 0L
        var highest = time
        var cnt = 0L
        while (lowest == 0L) {
            cnt++
            val holdDuration = cnt
            val driveDuration = time - cnt
            val distanceDone = holdDuration * driveDuration
            if (distanceDone > distance) {
                lowest = holdDuration
            }
        }
        cnt = time
        while (highest == time) {
            cnt--
            val holdDuration = cnt
            val driveDuration = time - cnt
            val distanceDone = holdDuration * driveDuration
            if (distanceDone > distance) {
                highest = holdDuration
            }
        }
        return highest-lowest+1L
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    val testInput2 = readInput("Day06_test")
    check(part2(testInput2) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
