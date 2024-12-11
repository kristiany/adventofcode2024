import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun main() {
    val input = listOf(0,27,5409930,828979,4471,3,68524,170) // Real
    //val input = listOf(0,1,10,99,999) // Test
    //val input = listOf(125,17) // Test2

    var cache = HashMap<Pair<Long, Int>, Long>()
    val result = input.map { it.toLong() }.sumOf { countNrs(it, 1, 25, 1, cache) }
    println("Part 1: $result")

    // Cache depends on max blinks so we need a fresh one
    cache = HashMap()
    val result2 = input.map { it.toLong() }.sumOf { countNrs(it, 1, 75, 1, cache) }
    println("Part 2: $result2")
}

fun countNrs(nr: Long, blink: Int, maxBlinks: Int, acc: Long, cache: HashMap<Pair<Long, Int>, Long>): Long {
    if (blink > maxBlinks) {
        return acc
    }
    val cacheKey = Pair(nr, blink)
    if (cache.contains(cacheKey)) {
        return cache[cacheKey]!!
    }
    if (nr == 0L) {
        val count = countNrs(1L, blink + 1, maxBlinks, acc, cache)
        cache[cacheKey] = count
        return count
    } else if (evenLength(nr)) {
        // String version should be slower, but it's not really noticeable
        //} else if (nr.toString().length % 2 == 0) {
            //val strVal = nr.toString()
            //val left = strVal.substring(0, strVal.length / 2).toLong()
            //val right = strVal.substring(strVal.length / 2, strVal.length).toLong()
        val halflength = length(nr) / 2
        val pow = 10.0.pow(halflength.toDouble()).toInt()
        val left = nr / pow
        val right = nr - left * pow
        val leftCount = countNrs(left, blink + 1, maxBlinks, acc, cache)
        val rightCount = countNrs(right, blink + 1, maxBlinks, 1, cache)
        cache[cacheKey] = leftCount + rightCount
        return leftCount + rightCount
    }
    val count = countNrs(nr * 2024, blink + 1, maxBlinks, acc, cache)
    cache[cacheKey] = count
    return count
}

// Faster than toString().length according to:
// https://code-maze.com/csharp-whats-the-best-way-to-count-the-number-of-digits-in-a-number/
private fun evenLength(number: Long): Boolean {
    if (number == 0L) return false
    return length(number) % 2 == 0
}

private fun length(number: Long): Int {
    if (number == 0L) return 1
    return 1 + floor(log10(number.toDouble())).toInt()
}