enum class Direction(val xChange: Int, val yChange: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    WEST(-1, 0),
    SOUTH(0, 1),
    NONE(0, 0)
}

enum class Pipe(val input: String, val possibleDirection: Pair<Direction, Direction>) {
    NORTH_SOUTH("|", Pair(Direction.NORTH, Direction.SOUTH)),
    WEST_EAST("-", Pair(Direction.EAST, Direction.WEST)),
    NORTH_EAST("L", Pair(Direction.NORTH, Direction.EAST)),
    NORTH_WEST("J", Pair(Direction.NORTH, Direction.WEST)),
    SOUTH_WEST("7", Pair(Direction.SOUTH, Direction.WEST)),
    SOUTH_EAST("F", Pair(Direction.SOUTH, Direction.EAST)),
    NOPE(".", Pair(Direction.NONE, Direction.NONE)),
    START("S", Pair(Direction.NONE, Direction.NONE))
    ;
}

fun main() {
    
    class Tile(pipeAsInput: Pipe, xKoord: Int, yKoord: Int) {
        var pipe = pipeAsInput
        val x = xKoord
        val y = yKoord
        var visited = false
        fun getPossibleDirection(): Pair<Direction, Direction> {
            return pipe.possibleDirection;
        }
    }

    open class Traveller(startingTile: Tile, map: List<List<Tile?>>) {
        var currentTile = startingTile
        var tileMap = map
        var currentSteps = 0
        var lastDirection = Direction.NONE

        val impossibleDirectionBasedOnLastDirection = mapOf(
            Direction.NORTH to Direction.SOUTH,
            Direction.SOUTH to Direction.NORTH,
            Direction.WEST to Direction.EAST,
            Direction.EAST to Direction.WEST
        )
        fun determineDirection(swapOtherDir: Boolean): Direction {
            val possibleDirection = currentTile.getPossibleDirection()
            val impossibleDirection = impossibleDirectionBasedOnLastDirection[lastDirection]
            return if (swapOtherDir) {
                if (possibleDirection.second == impossibleDirection) possibleDirection.first else possibleDirection.second
            } else {
                if (possibleDirection.first == impossibleDirection) possibleDirection.second else possibleDirection.first
            }
        }
        
        fun moveDirection(nextDirection: Direction) {
            currentSteps++
            lastDirection = nextDirection
            val newX = currentTile.x + nextDirection.xChange
            val newY = currentTile.y + nextDirection.yChange
            currentTile = tileMap[newY][newX]!!
        }
        open fun forceOtherDirection() { //only used for starting point
            moveDirection(determineDirection(true))
        }
        open fun moveIt() {
            moveDirection(determineDirection(false))
        }
    }

    fun part1(input: List<String>): Int {
        var xStart = 0
        var yStart = 0
        val tileMap = input.mapIndexed { yKoord, line ->
            line.mapIndexed() { xKoord, singleTile ->
                val tileCreated = Pipe.entries.find { it.input == singleTile.toString() }?.let { Tile(it, xKoord, yKoord) }
                if (singleTile == 'S') {
                    xStart = xKoord
                    yStart = yKoord
                    // determine possible direction
                    if (tileCreated != null) {
                        //hardcoded 
                        tileCreated.pipe = Pipe.SOUTH_EAST  //test

                    }
                }
                tileCreated
            }
        }.toList()
        val startingPoint = tileMap[yStart][xStart]
        if (startingPoint != null) {
            val firstTraveller = Traveller(startingPoint, tileMap)
            val secondTraveller = Traveller(startingPoint, tileMap)
            firstTraveller.moveIt()
            secondTraveller.forceOtherDirection()
            while (firstTraveller.currentTile != secondTraveller.currentTile) {
                firstTraveller.moveIt()
                secondTraveller.moveIt()
            }
            return firstTraveller.currentSteps
        }
        return 0
    }
    
    class TravellerMarker(startingTile: Tile, map: List<List<Tile?>>) : Traveller(startingTile, map) {
        override fun forceOtherDirection() {
            currentTile.visited = true
            super.forceOtherDirection()
        }

        override fun moveIt() {
            currentTile.visited = true
            super.moveIt()
        }
    }


    fun part2(input: List<String>): Int {
        var xStart = 0
        var yStart = 0
        val tileMap = input.mapIndexed { yKoord, line ->
            line.mapIndexed() { xKoord, singleTile ->
                val tileCreated = Pipe.entries.find { it.input == singleTile.toString() }?.let { Tile(it, xKoord, yKoord) }
                if (singleTile == 'S') {
                    xStart = xKoord
                    yStart = yKoord
                    // determine possible direction
                    if (tileCreated != null) {
                        //hardcoded 
                        tileCreated.pipe = Pipe.SOUTH_EAST  //test
                    }
                }
                tileCreated
            }
        }.toList()
        val startingPoint = tileMap[yStart][xStart]
        if (startingPoint != null) {
            val firstTraveller = TravellerMarker(startingPoint, tileMap)
            val secondTraveller = TravellerMarker(startingPoint, tileMap)
            firstTraveller.moveIt()
            secondTraveller.forceOtherDirection()
            while (firstTraveller.currentTile != secondTraveller.currentTile) {
                firstTraveller.moveIt()
                secondTraveller.moveIt()
            }
            // all tiles visited
            
            // now try to check for in loop
            var inLoop = false
            var cntInLoop = 0
            tileMap.forEach { line -> 
                line.forEach { tile -> 
                    if (tile!!.visited && tile.pipe == Pipe.NORTH_SOUTH) {
                        inLoop = !inLoop
                    }
                    if (inLoop && tile.pipe == Pipe.NOPE) {
                        cntInLoop++
                    }
                }
            }
            

            println("test")

        }
        return 0    }


//    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 4)
    val testInput2 = readInput("Day10_test")
    check(part2(testInput2) == 4)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
