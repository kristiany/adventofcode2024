fun main() {
    forLinesIn("day06/input.txt") { file ->
        val input = file.toList()
        val height = input.size
        val width = input[0].length
        val obstacles = HashSet<Pair<Int, Int>>()
        var guardStart = Guard(0, 0, 0)
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '#') {
                    obstacles.add(x to y)
                }
                else if (input[y][x] == '^') {
                    guardStart = Guard(x, y, 0) // Dir up
                }
            }
        }
        var guard = guardStart
        var steps = HashSet<Pair<Int, Int>>()
        steps.add(guard.x to guard.y)
        while (guard.insideMap(width, height)) {
            guard = advance(obstacles, guard)
            if (guard.insideMap(width, height)) {
                steps.add(guard.x to guard.y)
            }
        }
        println("Part 1: ${steps.size}")


        val maxSteps = steps.size * 2 // Magic number
        //println("Max steps ${maxSteps}")
        val loopingBlocks = HashSet<Pair<Int, Int>>()
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (obstacles.contains(x to y)) {
                    continue
                }
                val obstaclesWithBlock = HashSet(obstacles)
                obstaclesWithBlock.add(x to y)

                var guard = guardStart
                var actualSteps = 0
                steps.add(guard.x to guard.y)
                while (guard.insideMap(width, height) && actualSteps < maxSteps) {
                    guard = advance(obstaclesWithBlock, guard)
                    actualSteps++
                }
                if (actualSteps >= maxSteps) {
                    loopingBlocks.add(x to y)
                }
            }
        }
        println("Part 2: ${loopingBlocks.size}")

    }
}

private fun advance(obstacles: HashSet<Pair<Int, Int>>, guardPos: Guard): Guard {
    var guard = guardPos
    var next = guard.nextPos()
    while (obstacles.contains(next)) {
        guard = guard.turn()
        next = guard.nextPos()
    }
    return guard.fwd()
}

private data class Guard(val x: Int, val y: Int, val dir: Int) {

    fun nextPos(): Pair<Int, Int> {
        return when (dir) {
            0 -> x to y - 1
            1 -> x + 1 to y
            2 -> x to y + 1
            3 -> x - 1 to y
            else -> throw IllegalArgumentException()
        }
    }

    fun fwd(): Guard {
        val next = nextPos()
        return Guard(next.first, next.second, dir)
    }

    fun turn(): Guard {
        return Guard(x, y, (dir + 1) % 4)
    }

    fun insideMap(width: Int, height: Int): Boolean {
        return x < width && x >= 0 && y < height && y >= 0
    }
}

