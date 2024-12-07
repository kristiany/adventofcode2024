fun main() {
    forLinesIn("day07/input.txt") { file ->
        val input = file.map {
            val test = it.split(":")[0].toLong()
            val nrs = it.split(":")[1].trim().split(" ").map { it.toLong() }
            Equation(test, nrs)
        }.toList()
        println(input)

        var result = 0L
        for (eq in input) {
            //println(eq)
            val nrsStack = ArrayList(eq.nrs)
            val first = nrsStack.removeFirst()
            result += findOps(eq.test, first, nrsStack)
        }

        println("Part 1: $result")

        var result2 = 0L
        for (eq in input) {
            //println(eq)
            val nrsStack = ArrayList(eq.nrs)
            val first = nrsStack.removeFirst()
            result2 += findOps2(eq.test, first, nrsStack)
        }

        println("Part 2: $result2")
    }
}

private fun findOps(test: Long, acc: Long, nrs: ArrayList<Long>): Long {
    if (nrs.isEmpty()) {
        return acc
    }
    // Drop early, no chance
    if (acc > test) {
        return 0L
    }
    val right = nrs.removeFirst()
    if (findOps(test, acc * right, ArrayList(nrs)) == test) {
        return test
    }
    if (findOps(test, acc + right, ArrayList(nrs)) == test) {
        return test
    }
    return 0L
}

private fun findOps2(test: Long, acc: Long, nrs: ArrayList<Long>): Long {
    if (nrs.isEmpty()) {
        return acc
    }
    // Drop early, no chance
    if (acc > test) {
        return 0L
    }
    val right = nrs.removeFirst()
    if (findOps2(test, acc * right, ArrayList(nrs)) == test) {
        return test
    }
    if (findOps2(test, acc + right, ArrayList(nrs)) == test) {
        return test
    }
    val concat = (acc.toString() + right.toString()).toLong()
    if (findOps2(test, concat, ArrayList(nrs)) == test) {
        return test
    }
    return 0L
}

private data class Equation(val test: Long, val nrs: List<Long>)

