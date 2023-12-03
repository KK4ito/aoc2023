fun main() {

    fun notADigitAndNotADot(c: Char): Boolean {
        return !Character.isDigit(c) && c != '.'
    }

    fun rangeContainsAdjacentSymbol(intRange: IntRange, currentHeight: Int, input: List<String>): Boolean {
        val lineAbove = if (currentHeight != 0) input[currentHeight - 1] else null
        val currentLine = input[currentHeight]
        val lineBelow = if (currentHeight != input.size - 1) input[currentHeight + 1] else null
        for (i in intRange) {
            if (lineAbove != null && notADigitAndNotADot(lineAbove[i])) return true
            if (lineBelow != null && notADigitAndNotADot(lineBelow[i])) return true
        }
        if (intRange.first != 0) {
            if (notADigitAndNotADot(currentLine[intRange.first - 1])) return true
            if (lineAbove != null && notADigitAndNotADot(lineAbove[intRange.first - 1])) return true
            if (lineBelow != null && notADigitAndNotADot(lineBelow[intRange.first - 1])) return true

        }
        if (intRange.last != currentLine.length - 1) {
            if (notADigitAndNotADot(currentLine[intRange.last + 1])) return true
            if (lineAbove != null && notADigitAndNotADot(lineAbove[intRange.last + 1])) return true
            if (lineBelow != null && notADigitAndNotADot(lineBelow[intRange.last + 1])) return true
        }
        return false
    }
    
    fun part1(input: List<String>): Int {
        val digitRegex = Regex("\\d+")
        var sum = 0
        input.forEachIndexed { index, line ->
            val toMap = digitRegex.findAll(line).map { it.range to it.value }.toMap()
            toMap.forEach { (intRange, s) ->
                if (rangeContainsAdjacentSymbol(intRange, index, input)) {
                    sum += s.toInt()
                }
            }
        }
        return sum
    }

    fun checkForAlreadyUsed(
        entry: Map.Entry<IntRange, String>,
        index: Int,
        possibleMatches: MutableSet<IntRange>
    ): Boolean {
        if (entry.key.contains(index) && !possibleMatches.contains(entry.key)) {
            possibleMatches.add(entry.key)
            return true
        } else {
            return false
        }
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val digitRegex = Regex("\\d+")
        val mapNumberWithRange = input
            .mapIndexed { index, line -> index to digitRegex.findAll(line).associate { it.range to it.value } }
            .toMap()

        val indicesWithSpecialChar = input.mapIndexedNotNull { index, str ->
            if (str.contains("*")) index else null
        }

        indicesWithSpecialChar.forEach { idx ->
            var indexOfGear = input[idx].indexOf("*")
            while (indexOfGear != -1) {
                val listOfApproxNumber = mutableListOf<Int>()
                // check currentLine for numbers
                val currentLineWithNumbers = mapNumberWithRange[idx]
                val rightOfGear = indexOfGear + 1
                val leftOfGear = indexOfGear - 1
                if (!currentLineWithNumbers.isNullOrEmpty()) {
                    val rightNumber =
                        currentLineWithNumbers.entries.firstOrNull { it.key.contains(rightOfGear) }?.value?.toInt()
                    val leftNumber =
                        currentLineWithNumbers.entries.firstOrNull { it.key.contains(leftOfGear) }?.value?.toInt()
                    if (rightNumber != null) listOfApproxNumber.add(rightNumber)
                    if (leftNumber != null) listOfApproxNumber.add(leftNumber)
                }

                // check above line only if current line is not 0
                if (idx != 0) {
                    val lineAboveWithNumbers = mapNumberWithRange[idx - 1]
                    val possibleMatches = mutableSetOf<IntRange>()
                    if (lineAboveWithNumbers != null) {
                        val rightAboveNumber =
                            lineAboveWithNumbers.entries.firstOrNull { entry ->
                                checkForAlreadyUsed(
                                    entry,
                                    rightOfGear,
                                    possibleMatches
                                )
                            }?.value?.toInt()
                        val middleAboveNumber =
                            lineAboveWithNumbers.entries.firstOrNull { entry ->
                                checkForAlreadyUsed(
                                    entry,
                                    indexOfGear,
                                    possibleMatches
                                )
                            }?.value?.toInt()
                        val leftAboveNumber =
                            lineAboveWithNumbers.entries.firstOrNull { entry ->
                                checkForAlreadyUsed(
                                    entry,
                                    leftOfGear,
                                    possibleMatches
                                )
                            }?.value?.toInt()
                        if (rightAboveNumber != null) listOfApproxNumber.add(rightAboveNumber)
                        if (middleAboveNumber != null) listOfApproxNumber.add(middleAboveNumber)
                        if (leftAboveNumber != null) listOfApproxNumber.add(leftAboveNumber)
                    }
                }

                // check below line only if current line is not last
                if (idx != input.size) {
                    val possibleMatches = mutableSetOf<IntRange>()
                    val lineBelowWithNumbers = mapNumberWithRange[idx + 1]
                    if (lineBelowWithNumbers != null) {
                        val rightBelowNumber =
                            lineBelowWithNumbers.entries.firstOrNull { entry ->
                                checkForAlreadyUsed(
                                    entry,
                                    rightOfGear,
                                    possibleMatches
                                )
                            }?.value?.toInt()
                        val middleBelowNumber =
                            lineBelowWithNumbers.entries.firstOrNull { entry ->
                                checkForAlreadyUsed(
                                    entry,
                                    indexOfGear,
                                    possibleMatches
                                )
                            }?.value?.toInt()
                        val leftBelowNumber =
                            lineBelowWithNumbers.entries.firstOrNull { entry ->
                                checkForAlreadyUsed(
                                    entry,
                                    leftOfGear,
                                    possibleMatches
                                )
                            }?.value?.toInt()
                        if (rightBelowNumber != null) listOfApproxNumber.add(rightBelowNumber)
                        if (middleBelowNumber != null) listOfApproxNumber.add(middleBelowNumber)
                        if (leftBelowNumber != null) listOfApproxNumber.add(leftBelowNumber)
                    }
                }

                // all lines checked, list must size=2
                if (listOfApproxNumber.size == 2) {
                    sum += listOfApproxNumber.reduce { sum, value -> sum * value }
                }
                indexOfGear = input[idx].indexOf("*", indexOfGear + 1)// in case of multiple gears$
            }
        }
        return sum
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    val testInput2 = readInput("Day03_test")
    check(part2(testInput2) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
