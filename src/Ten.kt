fun main() {
    forLinesIn("day10/input.txt") { file ->
        val map = file.map { line -> line.map { if (it == '.') -1 else it.toString().toInt() }}
            .toList()
        var scores = 0
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == 0) {
                    val trails = findTrails(map, x, y, 0, ArrayList())
                    scores += HashSet(trails).size
                }
            }
        }
        println("Part 1: $scores")

        var scores2 = 0
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == 0) {
                    val trails = findTrails(map, x, y, 0, ArrayList())
                    scores2 += trails.size
                }
            }
        }
        println("Part 2: $scores2")
    }
}

private fun findTrails(map: List<List<Int>>, x: Int, y: Int, cur: Int, ends: ArrayList<Pair<Int, Int>>): List<Pair<Int, Int>> {
    if (map[y][x] == 9 && cur == 9) {
        ends.add(x to y)
        return ends
    }
    val ways = ways(map, x, y, cur + 1)
    for (way in ways) {
        findTrails(map, way.first, way.second, cur + 1, ends)
    }
    return ends
}

private fun ways(map: List<List<Int>>, x: Int, y: Int, next: Int): List<Pair<Int, Int>> {
    val result = ArrayList<Pair<Int, Int>>()
    if (x > 0 && map[y][x - 1] == next) result.add(x - 1 to y)
    if (x < map[0].size - 1 && map[y][x + 1] == next) result.add(x + 1 to y)
    if (y > 0 && map[y - 1][x] == next) result.add(x to y - 1)
    if (y < map.size - 1 && map[y + 1][x] == next) result.add(x to y + 1)
    return result
}

