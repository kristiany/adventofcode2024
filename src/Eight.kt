fun main() {
    forLinesIn("day08/input.txt") { file ->
        val input = file.toList()
        val antennas = input.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c != '.') Antenna(x, y, c)
                else null
            }.filterNotNull()
        }.toList()

        //println(antennas)
        val width = input[0].length
        val height = input.size
        val result = antinodes(antennas, width, height)
        //println(result)
        //debug(width, height, antennas, result)

        println("Part 1: ${result.size}")

        val result2 = antinodes2(antennas, width, height)
        //debug(width, height, antennas, result2)
        println("Part 2: ${result2.size}")

    }
}

private fun debug(
    width: Int,
    height: Int,
    antennas: List<Antenna>,
    antinodes: HashSet<Pair<Int, Int>>
) {
    for (y in 0..<height) {
        for (x in 0..<width) {
            val a = antennas.find { it.x == x && it.y == y }
            print(
                if (antinodes.contains(x to y)) '#'
                else a?.freq ?: '.'
            )
        }
        println()
    }
}

private fun antinodes(
    antennas: List<Antenna>,
    width: Int,
    height: Int
): HashSet<Pair<Int, Int>> {
    val antinodes = HashSet<Pair<Int, Int>>()
    for (g in antennas.groupBy { it.freq }) {
        if (g.value.size > 1) {
            for (i in 1..<g.value.size) {
                val a1 = g.value[i - 1]
                for (a in g.value.subList(i, g.value.size)) {
                    val v = a1.x - a.x to a1.y - a.y
                    // a1 vector added to a1, negative vector to the other
                    val ad1 = a1.x + v.first to a1.y + v.second
                    if (inside(ad1, width, height))
                        antinodes.add(ad1)
                    val ad2 = a.x - v.first to a.y - v.second
                    if (inside(ad2, width, height))
                        antinodes.add(ad2)
                }
            }
        }
    }
    return antinodes
}

private fun antinodes2(
    antennas: List<Antenna>,
    width: Int,
    height: Int
): HashSet<Pair<Int, Int>> {
    val antinodes = HashSet<Pair<Int, Int>>()
    for (g in antennas.groupBy { it.freq }) {
        if (g.value.size > 1) {
            for (i in 1..<g.value.size) {
                val a1 = g.value[i - 1]
                antinodes.add(a1.x to a1.y)
                for (a in g.value.subList(i, g.value.size)) {
                    antinodes.add(a.x to a.y)
                    val v = a1.x - a.x to a1.y - a.y
                    // a1 vector added to a1, negative vector to the other
                    findNodes(a1, v, width, height, antinodes)
                    findNodes(a, -v.first to -v.second, width, height, antinodes)
                }
            }
        }
    }
    return antinodes
}

private fun findNodes(
    a: Antenna,
    v: Pair<Int, Int>,
    width: Int,
    height: Int,
    antinodes: HashSet<Pair<Int, Int>>
) {
    var inside = true
    var ad = a.x to a.y
    while (inside) {
        ad = ad.first + v.first to ad.second + v.second
        if (inside(ad, width, height))
            antinodes.add(ad)
        else
            inside = false
    }
}

private fun inside(p: Pair<Int, Int>, width: Int, height: Int): Boolean {
    return p.first in 0..< width && p.second in 0..< height
}

private data class Antenna(val x: Int, val y: Int, val freq: Char)
