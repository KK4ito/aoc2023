import kotlin.math.abs

fun main() {

    class Matrix(val data: MutableList<MutableList<Boolean>>) {
        val numRows: Int get() = data.size
        val numCols: Int get() = if (numRows > 0) data[0].size else 0

        operator fun get(row: Int, col: Int): Boolean = data[row][col]

        fun row(rowIndex: Int): List<Boolean> = data[rowIndex]

        fun col(colIndex: Int): List<Boolean> = data.map { it[colIndex] }

        fun addRowAtIndex(index: Int, newRow: List<Boolean>) {
            data.add(index, newRow.toMutableList())
        }

        fun addColumnAtIndex(index: Int, newCol: List<Boolean>) {
            newCol.forEachIndexed { rowIndex, value -> data[rowIndex].add(index, value) }
        }

        fun findTrueCoordinates(): List<Pair<Int, Int>> {
            val trueCoordinates = mutableListOf<Pair<Int, Int>>()

            for (i in 0 until numRows) {
                for (j in 0 until numCols) {
                    if (data[i][j]) {
                        trueCoordinates.add(i to j)
                    }
                }
            }

            return trueCoordinates
        }


        fun printConsole() {
            data.forEach { yLine ->
                yLine.forEach { xPoint -> print(if (xPoint) "#" else ".") }
                println("")
            }
        }
    }

    fun getDiff(galaxyFirst: Pair<Int, Int>, galaxySecond: Pair<Int, Int>): Long {
        val differenceX = abs(galaxyFirst.first - galaxySecond.first).toLong()
        val differenceY = abs(galaxyFirst.second - galaxySecond.second).toLong()
        return differenceX + differenceY
    }

    fun createPairPermutations(coordinates: List<Pair<Int, Int>>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val result = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        for (i in coordinates.indices) {
            for (j in (i + 1) until coordinates.size) {
                val pair1 = coordinates[i]
                val pair2 = coordinates[j]
                result.add(pair1 to pair2)
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val originalMap = input.map { yLine -> yLine.map { xPoint -> xPoint == '#' }.toMutableList() }.toMutableList()
        val matrixBasedOnOG = Matrix(originalMap)
        var i = 0
        var initEnd = matrixBasedOnOG.numRows
        while (i < initEnd) {
            if (matrixBasedOnOG.row(i).all { !it }) {
                matrixBasedOnOG.addRowAtIndex(i, List(matrixBasedOnOG.row(i).size) { false })
                i++
                initEnd++
            }
            i++
        }

        i = 0
        initEnd = matrixBasedOnOG.numCols
        while (i < initEnd) {
            if (matrixBasedOnOG.col(i).all { !it }) {
                matrixBasedOnOG.addColumnAtIndex(i, List(matrixBasedOnOG.col(i).size) { false })
                i++
                initEnd++
            }
            i++

        }
        return 0
//        return createPairPermutations(matrixBasedOnOG.findTrueCoordinates()).sumOf { getDiff(it.first, it.second) }
    }

    data class Point2D(val x: Int, val y: Int)
    
    fun part2(input: List<String>): Long {
        val expandBy = 10000
        val sumRows = input.runningFold(0) { prev, row -> if (row.any { it == '#' }) prev else prev + 1 }

        val sumCols = input.first().indices.map { colIdx -> input.map { row -> row[colIdx] } }
            .runningFold(0) { prev, col -> if (col.any { it == '#' }) prev else prev + 1 }

        val galaxies = input.flatMapIndexed { rowIdx, row ->
            row.mapIndexedNotNull { colIdx, c ->
                if (c == '#') {
                    Point2D(
                        x = colIdx + sumCols[colIdx] * (expandBy - 1),
                        y = rowIdx + sumRows[rowIdx] * (expandBy - 1)
                    )
                } else null
            }
        }

        return (0..<galaxies.size - 1).sumOf { i ->
            (i + 1..<galaxies.size).sumOf { j ->
                abs(galaxies[j].x - galaxies[i].x) + abs(galaxies[j].y - galaxies[i].y).toLong()
            }
        }
    }

//
//    val testInput = readInput("Day11_test")
//    check(part1(testInput) == 374)
//    val testInput2 = readInput("Day11_test")
//    check(part2(testInput2) == 1030L)

    val input = readInput("Day11")
//    part1(input).println()
    part2(input).println()
}
