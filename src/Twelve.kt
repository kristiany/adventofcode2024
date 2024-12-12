import kotlin.math.abs

fun main() {
    forLinesIn("day12/input.txt") { file ->
        val input = file.toList()
        val posParsed = HashSet<Pair<Int, Int>>()
        val regions = ArrayList<Region>()
        for (y in input.indices) {
            for (x in input[0].indices) {
                if (! posParsed.contains(x to y)) {
                    val region = parseRegion(input, input[y][x], x, y, HashSet())
                    regions.add(region)
                    posParsed.addAll(region.plots)
                }
            }
        }
        //println(regions)
        val result = regions.sumOf { it.price() }
        println("Part 1: $result")

        val result2 = regions.sumOf { bulkPrice(it) }
        println("Part 2: $result2")

    }
}

/*
    Edge
      0
    3   1
      2
*/
private data class Side(val edge: Int, val from: Pair<Int, Int>, val to: Pair<Int, Int>) {
    fun adjacentTo(other: Side): Boolean {
        // Other will always have same from and to
        return other.from.first == from.first && abs(other.to.second - to.second) == 1
                || other.from.second == to.second && abs(other.to.first - to.first) == 1
    }
}

private fun bulkPrice(r: Region): Long {
    val sides = r.plots.flatMap { p ->
        var result = HashSet<Side>()
        if (! r.plots.contains(p.first + 1 to p.second))
            result.add(Side(1, p, p))
        if (! r.plots.contains(p.first - 1 to p.second))
            result.add(Side(3, p, p))
        if (! r.plots.contains(p.first to p.second + 1))
            result.add(Side(2, p, p))
        if (! r.plots.contains(p.first to p.second - 1))
            result.add(Side(0, p, p))
        result
    }.toSet()

    val reduced = sides.sortedWith(compareBy({ it.from.first }, { it.from.second }))
        .fold(ArrayList<Side>()) { acc, side ->
            var notFound = true
            for (i in acc.indices) {
                val fullSide = acc[i]
                if (side.edge == fullSide.edge && fullSide.adjacentTo(side)) {
                    if (fullSide.from.first < side.to.first || fullSide.from.second < side.to.second) {
                        acc[i] = Side(fullSide.edge, fullSide.from, side.to)
                    }
                    else {
                        acc[i] = Side(side.edge, side.from, fullSide.to)
                    }
                    notFound = false
                    break
                }
            }
            if (notFound)
                acc.add(side)
            acc
        }
    return r.plots.size.toLong() * reduced.size
}

private fun parseRegion(input: List<String>, type: Char, x: Int, y: Int, acc: HashSet<Pair<Int, Int>>): Region {
    acc.add(x to y)
    for (dir in ways(input, x, y, type, acc)) {
        parseRegion(input, type, dir.first, dir.second, acc)
    }
    return Region.of(type, acc)
}

private data class Region(val type: Char,
                          val plots: Set<Pair<Int, Int>>,
                          val perimeter: Int) {

    companion object {
        fun of(type: Char, plots: Set<Pair<Int, Int>>): Region {
            return Region(type, plots, perimeter(plots))
        }

        private fun perimeter(plots: Set<Pair<Int, Int>>): Int {
            var result = 0
            for (p in plots) {
                if (open(plots, p.first + 1 to p.second))
                    ++result
                if (open(plots, p.first - 1 to p.second))
                    ++result
                if (open(plots, p.first to p.second + 1))
                    ++result
                if (open(plots, p.first to p.second - 1))
                    ++result
            }
            return result
        }

        private fun open(
            plots: Set<Pair<Int, Int>>,
            p: Pair<Int, Int>
        ) = !plots.contains(p)
    }

    fun price(): Long {
        return plots.size * perimeter.toLong()
    }

}

private fun ways(map: List<String>, x: Int, y: Int, type: Char, acc: HashSet<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val result = ArrayList<Pair<Int, Int>>()
    if (x > 0 && map[y][x - 1] == type && ! acc.contains(x - 1 to y))
        result.add(x - 1 to y)
    if (x < map[0].length - 1 && map[y][x + 1] == type && ! acc.contains(x + 1 to y))
        result.add(x + 1 to y)
    if (y > 0 && map[y - 1][x] == type && ! acc.contains(x to y - 1))
        result.add(x to y - 1)
    if (y < map.size - 1 && map[y + 1][x] == type && ! acc.contains(x to y + 1))
        result.add(x to y + 1)
    return result
}