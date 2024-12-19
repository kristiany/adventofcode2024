import kotlin.math.min

fun main() {
    forLinesIn("day19/input.txt") { row ->
        val input = row.toList()
        val patterns = input[0].split(", ").toSet()
        val maxPatternLength = patterns.maxOf { it.length }
        val designs = input.drop(2).toList()
        println(patterns)
        println(designs)

        val cache = HashMap<String, Long>()
        val valid = designs.mapIndexed { index, design ->
            //println("$index $design")
            towelable(patterns, maxPatternLength, design, cache) > 0
        }.count { it }

        println("Part 1: $valid")

        val valid2 = designs.mapIndexed { index, design ->
            //println("$index $design")
            towelable(patterns, maxPatternLength, design, cache)
        }.sumOf { it }

        println("Part 2: $valid2")

    }
}

private fun towelable(patterns: Set<String>,
                      maxPatternLength: Int,
                      design: String,
                      cache: HashMap<String, Long>): Long {
    if (design.isBlank()) {
        return 1
    }
    if (cache.containsKey(design)) {
        return cache[design]!!
    }
    var result = 0L
    val stop = min(maxPatternLength, design.length)
    for (i in stop downTo  1) {
        val value = design.take(i)
        if (patterns.contains(value)) {
            val towelable = towelable(patterns, maxPatternLength, design.drop(i), cache)
            result += towelable
            cache[design.drop(i)] = towelable
        }
    }
    return result
}

