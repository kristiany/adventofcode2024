import kotlin.math.abs

fun main() {
    forLinesIn("day18/input.txt") { file ->
        val bytes = file.map { it.split(",")[0].toInt() to it.split(",")[1].toInt() }.toList()
        val dim = 70 // real input
        val bytesFallenCount = 1024
        //val dim = 6  // test
        //val bytesFallenCount = 12

        println(bytes)

        val bytesFallen = bytes.subList(0, bytesFallenCount).toHashSet()
        debugMem(dim, bytesFallen)

        val steps = shortest(dim, bytesFallen)
        println("Part 1: $steps")

        for (byte in bytes.subList(bytesFallenCount, bytes.size)) {
            bytesFallen.add(byte)
            val steps = shortest(dim, bytesFallen)
            if (steps < 0) {
                println("Part 2: $byte")
                break
            }
        }
    }
}

// A* https://en.wikipedia.org/wiki/A*_search_algorithm#:~:text=A*%20is%20an%20informed%20search,shortest%20time%2C%20etc.).
fun shortest(dim: Int, bytesFallen: Set<Pair<Int, Int>>): Int {
    val start = 0 to 0
    val end = dim to dim
    val openSet = ArrayList<Pair<Int, Int>>(listOf(start))
    val cameFrom = HashMap<Pair<Int, Int>, Pair<Int, Int>>()
    val gscore = HashMap<Pair<Int, Int>, Int>()
    gscore[start] = 0
    val fscore = HashMap<Pair<Int, Int>, Int>()
    fscore[start] = h(start, end)
    while (openSet.isNotEmpty()) {
        val current = openSet.removeFirst()
        if (current == end)
            return path(cameFrom, current).size - 1

        for (neighbor in listOf(
            current.first + 1 to current.second,
            current.first - 1 to current.second,
            current.first to current.second + 1,
            current.first to current.second - 1,
        ).filter { validPos(it, dim, bytesFallen) }) {
            val tentativeGscore = gscore.getOrDefault(current, 1e9.toInt()) + 1 // d(current, neighbor) is always one step
            if (tentativeGscore < gscore.getOrDefault(neighbor, 1e9.toInt())) {
                cameFrom[neighbor] = current
                gscore[neighbor] = tentativeGscore
                fscore[neighbor] = tentativeGscore + h(neighbor, end)
                if (! openSet.contains(neighbor)) {
                    openSet.add(neighbor)
                    openSet.sortBy { fscore.getOrDefault(it, 1e9.toInt()) }
                }
            }
        }
    }
    return -1 // Fail
}

private fun validPos(it: Pair<Int, Int>, dim: Int, bytesFallen: Set<Pair<Int, Int>>) =
    it.first >= 0 && it.first <= dim && it.second >= 0 && it.second <= dim
            && ! bytesFallen.contains(it)

fun h(p: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    // Manhattan distance
    return abs(end.first - p.first) + abs(end.second - p.second)
}

private fun path(cameFrom: Map<Pair<Int, Int>, Pair<Int, Int>>, current: Pair<Int, Int>): List<Pair<Int, Int>> {

    val result = ArrayList<Pair<Int, Int>>(listOf(current))
    var it = current
    while (cameFrom.keys.contains(it)) {
        it = cameFrom[it]!!
        result.add(current)
    }
    return result.reversed()
}


private fun debugMem(dim: Int, bytes: Set<Pair<Int, Int>>) {
    for (y in 0 .. dim) {
        for (x in 0 .. dim) {
            if (bytes.contains(x to y))
                print("#")
            else
                print(".")
        }
        println()
    }
}