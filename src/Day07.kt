enum class HandType(val power: Int) { //less power stronger
    FIVE_OF_A_KIND(100),
    FOUR_OF_A_KIND(50),
    FULL_HOUSE(25),
    THREE_OF_A_KIND(12),
    TWO_PAIR(6),
    ONE_PAIR(3),
    HIGH_CARD(1)
}

fun main() {
    val powerOfNonNumbers = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10
    )

    fun checkAdditionalHighestThree(handAsList: List<Pair<Char, Int>>): HandType {
        return when (handAsList[1].second) {
            2 -> HandType.FULL_HOUSE
            else -> HandType.THREE_OF_A_KIND
        }
    }


    fun checkAdditionalHighestTwo(handAsList: List<Pair<Char, Int>>): HandType {
        return when (handAsList[1].second) {
            2 -> HandType.TWO_PAIR
            else -> HandType.ONE_PAIR
        }
    }

    fun checkAdditionalHighestThreeAndWildCard(handAsList: List<Pair<Char, Int>>, wildcardCount: Int): HandType {
        return when (handAsList[1].second) {
            2  -> HandType.FULL_HOUSE
            else -> HandType.THREE_OF_A_KIND
        }
    }


    fun checkAdditionalHighestTwoAndWildCard(handAsList: List<Pair<Char, Int>>, wildcardCount: Int): HandType {
        return when (handAsList[1].second) {
            2  -> HandType.TWO_PAIR
            else -> HandType.ONE_PAIR
        }
    }


    open class Hand(handAsString: String, scoreInput: Int) : Comparable<Hand> {
        val hand = handAsString
        val score = scoreInput

        open fun getHandType(): HandType {
            val handMap = mutableMapOf<Char, Int>()
            hand.forEach { char -> handMap.compute(char) { _, count -> count?.plus(1) ?: 1 } }
            val sortedByDescending = handMap.toList().sortedByDescending { it.second }
            return when (sortedByDescending.first().second) {
                5 -> HandType.FIVE_OF_A_KIND
                4 -> HandType.FOUR_OF_A_KIND
                3 -> checkAdditionalHighestThree(sortedByDescending)
                2 -> checkAdditionalHighestTwo(sortedByDescending)
                else -> HandType.HIGH_CARD
            }
        }
        override fun compareTo(other: Hand): Int {
            val handTypeCheck = this.getHandType().power.compareTo(other.getHandType().power)

            if (handTypeCheck == 0) { // check each one
                val comparisonResult = this.hand.zip(other.hand)
                    .asSequence()
                    .map { (currHand, otherHand) ->
                        val currScore = if (currHand.isDigit()) currHand.digitToInt() else powerOfNonNumbers[currHand]
                        val otherScore =
                            if (otherHand.isDigit()) otherHand.digitToInt() else powerOfNonNumbers[otherHand]
                        var resultCompare = 0
                        if (currScore != null && otherScore != null) {
                            resultCompare = currScore.compareTo(otherScore)
                        }
                        resultCompare
                    }
                    .find { it != 0 } // find the first non-zero comparison result

                // Now you can use comparisonResult
                if (comparisonResult == null) {
                    println("i fucked up")// The lists are equal up to this point
                    // Additional logic for when all elements are equal
                    return 0
                } else {
                    return comparisonResult
                }
            }
            return handTypeCheck
        }
    }


    fun part1(input: List<String>): Int {
        val handWithTypes = mutableListOf<Hand>()
        input.forEach { handWithScore ->
            val lineParsed = handWithScore.split(" ")
            val hand = Hand(lineParsed[0], lineParsed[1].toInt())
            handWithTypes.add(hand)
        }
        val sortedBy = handWithTypes.sortedByDescending { it }
        var result = 0
        sortedBy.forEachIndexed { index, value ->
            result += value.score * (sortedBy.size - index)
        }
        return result
    }


    val powerOfNonNumbersWildCard = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 1,
        'T' to 10
    )

    class HandWithWildCards(handAsString: String, scoreInput: Int) : Hand(handAsString, scoreInput) {
        val countWildCards = handAsString.count { it == 'J' }
        
        override fun compareTo(other: Hand): Int {
            val handTypeCheck = this.getHandType().power.compareTo(other.getHandType().power)

            if (handTypeCheck == 0) { // check each one
                val comparisonResult = this.hand.zip(other.hand)
                    .asSequence()
                    .map { (currHand, otherHand) ->
                        val currScore = if (currHand.isDigit()) currHand.digitToInt() else powerOfNonNumbersWildCard[currHand]
                        val otherScore =
                            if (otherHand.isDigit()) otherHand.digitToInt() else powerOfNonNumbersWildCard[otherHand]
                        var resultCompare = 0
                        if (currScore != null && otherScore != null) {
                            resultCompare = currScore.compareTo(otherScore)
                        }
                        resultCompare
                    }
                    .find { it != 0 } // find the first non-zero comparison result

                // Now you can use comparisonResult
                if (comparisonResult == null) {
                    println("i fucked up")// The lists are equal up to this point
                    // Additional logic for when all elements are equal
                    return 0
                } else {
                    return comparisonResult
                }
            }
            return handTypeCheck
        }

        override fun getHandType(): HandType {
            val handMap = mutableMapOf<Char, Int>()
            hand.forEach { char -> handMap.compute(char) { _, count -> count?.plus(1) ?: 1 } }
            handMap['J'] = 0 // fuck you too
            val sortedByDescending = handMap.toList().sortedByDescending { it.second }
            if (hand == "JJJJJ") {
                return HandType.FIVE_OF_A_KIND
            } // fuck you
            return when (sortedByDescending.first().second) {
                5 - countWildCards -> HandType.FIVE_OF_A_KIND
                4 - countWildCards -> HandType.FOUR_OF_A_KIND
                3 - countWildCards -> checkAdditionalHighestThreeAndWildCard(sortedByDescending, countWildCards)
                2 - countWildCards -> checkAdditionalHighestTwoAndWildCard(sortedByDescending, countWildCards)
                else -> HandType.HIGH_CARD
            }
        }
    }

    fun part2(input: List<String>): Int {
        val handWithTypes = mutableListOf<HandWithWildCards>()
        input.forEach { handWithScore ->
            val lineParsed = handWithScore.split(" ")
            val hand = HandWithWildCards(lineParsed[0], lineParsed[1].toInt())
            handWithTypes.add(hand)
        }
        val sortedBy = handWithTypes.sortedBy { it }
        var result = 0
        sortedBy.forEachIndexed { index, value ->
            result += value.score * index+1
        }
        sortedBy[1].compareTo(sortedBy[2])
        return result
    }


    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day07_test")
//    check(part1(testInput) == 6440)
    val testInput2 = readInput("Day07_test")
    check(part2(testInput2) == 6839)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
