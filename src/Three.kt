fun main() {
    forLinesIn("day03/input.txt") { lines ->

        val input = lines.toList()
        val reg = Regex("mul\\([0-9]{1,3},[0-9]{1,3}\\)")
        val result = input.flatMap { reg.findAll(it) }
            .map { it.value }
            .map { parseMul(it) }
            .sumOf { it.first * it.second }

        println("Part 1: $result")

        val reg2 = Regex("do\\(\\)|don't\\(\\)|mul\\([0-9]{1,3},[0-9]{1,3}\\)")
        val code = input.flatMap { reg2.findAll(it) }
            .map { it.value }
            .toList()
        var enabled = true
        var sum = 0
        for (c in code) {
            if (c.equals("don't()")) {
                enabled = false
            }
            if (c.equals("do()")) {
                enabled = true
            }
            if (enabled && c.startsWith("mul")) {
                val m = parseMul(c)
                sum += m.first * m.second
            }
        }
        println("Part 2: $sum")

        // Second solution of Part 2 after submit just to see how to avoid ye standard for loop
        val result2 = code.fold(true to 0) { acc, code ->
            if ("don't()".equals(code)) false to acc.second
            else if ("do()".equals(code)) true to acc.second
            else if (acc.first) {
                val m = parseMul(code)
                acc.first to acc.second + m.first * m.second
            }
            else acc
        }
        println("Part 2 (functionalized): ${result2.second}")
    }

}

private fun parseMul(code: String): Pair<Int, Int> {
    return code.split(",")[0]
        .split("(")[1]
        .toInt() to code.split(",")[1]
        .split(")")[0]
        .toInt()
}
