fun main() {
    forLinesIn("day04/input.txt") { lines ->
        val input = lines.toList()
        var found = HashSet<Set<Pair<Int, Int>>>()
        for (y in input.indices) {
            for (x in input[0].indices) {
                if (input[y][x] == 'X') {
                    findXmas(input, x, y, found, "MAS")
                } else if (input[y][x] == 'S') {
                    findXmas(input, x, y, found, "AMX")
                }
            }
        }
        //debug(input, found)

        println("Part 1: ${found.size}")

        var found2 = HashSet<Set<Pair<Int, Int>>>()
        for (y in input.indices) {
            for (x in input[0].indices) {
                if (input[y][x] == 'A') {
                    findCrossmas(input, x, y, found2)
                }
            }
        }
        //debug(input, found2)
        println("Part 2: ${found2.size}")
    }
}

private fun debug(input: List<String>, found: Set<Set<Pair<Int, Int>>>) {
    println(found)
    for (y in input.indices) {
        for (x in input[0].indices) {
            val cell = found.any { it.contains(x to y) }
            if (cell) {
                print("*")
            } else {
                print(".")
            }
        }
        println()
    }
}

private fun findXmas(
    input: List<String>,
    x: Int,
    y: Int,
    map: HashSet<Set<Pair<Int, Int>>>,
    str: String
) {

    for (adj in adjaacents(x, y, input[y].length, input.size)) {
        if (input[adj.y][adj.x] == str[0]) {
            val step1 = adj.step(input[y].length, input.size)
            val step2 = step1?.step(input[y].length, input.size)
            if (step1 != null && step2 != null) {
                if (input[step1.y][step1.x] == str[1]
                    && input[step2.y][step2.x] == str[2]
                ) {
                    // Bingo!
                    map.add(setOf(x to y, adj.x to adj.y, step1.x to step1.y, step2.x to step2.y))
                }
            }
        }
    }
}

fun findCrossmas(
    input: List<String>,
    x: Int,
    y: Int,
    map: HashSet<Set<Pair<Int, Int>>>
) {

    val adjaacentsCross = adjaacentsCross(x, y, input[y].length, input.size)
    if (adjaacentsCross.size < 4) {
        // Nope not gonna fly, bye bye!
        return
    }
    val chars = adjaacentsCross.map { input[it.y][it.x] }.toList()
    // Validate the cross
    if (! (chars.count { it == 'M' } == 2 && chars.count { it == 'S' } == 2)) {
        return
    }
    for (adj in adjaacentsCross) {
        val opposite = Adj(x, y, adj.dirx, adj.diry).step(input[y].length, input.size)!! // Should not be null here
        if (input[adj.y][adj.x] == input[opposite.y][opposite.x]) {
            return
        }
    }
    // Bingo!
    val result = adjaacentsCross.map { it.x to it.y }.toHashSet()
    result.add(x to y)
    map.add(result)
}

private data class Adj(val x: Int, val y: Int, val dirx: Int, val diry: Int) {
    fun step(xsize: Int, ysize: Int): Adj? {
        if (x + dirx > xsize - 1
            || y + diry > ysize - 1
            || x + dirx < 0
            || y + diry < 0
        ) {
            return null
        }
        return Adj(x + dirx, y + diry, dirx, diry)
    }
}

private fun adjaacents(x: Int, y: Int, xsize: Int, ysize: Int): List<Adj> {
    val result = ArrayList<Adj>()
    if (y < ysize - 1)
        result.add(Adj(x, y + 1, 0, 1))
    if (y > 0)
        result.add(Adj(x, y - 1, 0, -1))
    if (x < xsize - 1)
        result.add(Adj(x + 1, y, 1, 0))
    if (x > 0)
        result.add(Adj(x - 1, y, -1, 0))
    if (y < ysize - 1 && x < xsize - 1)
        result.add(Adj(x + 1, y + 1, 1, 1))
    if (y < ysize - 1 && x > 0)
        result.add(Adj(x - 1, y + 1, -1, 1))
    if (y > 0 && x < xsize - 1)
        result.add(Adj(x + 1, y - 1, 1, -1))
    if (y > 0 && x > 0)
        result.add(Adj(x - 1, y - 1, -1, -1))
    return result;
}

private fun adjaacentsCross(x: Int, y: Int, xsize: Int, ysize: Int): List<Adj> {
    val result = ArrayList<Adj>()
    if (y < ysize - 1 && x < xsize - 1)
        result.add(Adj(x + 1, y + 1, -1, -1))
    if (y < ysize - 1 && x > 0)
        result.add(Adj(x - 1, y + 1, 1, -1))
    if (y > 0 && x < xsize - 1)
        result.add(Adj(x + 1, y - 1, -1, 1))
    if (y > 0 && x > 0)
        result.add(Adj(x - 1, y - 1, 1, 1))
    return result;
}
