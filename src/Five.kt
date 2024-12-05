fun main() {
    forLinesIn("day05/input.txt") { row ->
        val input = row.toList()
        val rules = HashMap<Int, HashSet<Int>>()
        input.filter { it.contains("|") }
            .forEach {
                val key = it.split("|")[0].toInt()
                val value = it.split("|")[1].toInt()
                // Flipping key-value for better rules access
                rules.computeIfAbsent(value, { _ -> HashSet() }).add(key)
            }
        val updates = input.filter { it.contains(",") }
            .map { it.split(",").map { it.toInt() }.toList() }
            .toList()

        val result = updates.filter { valid(rules, it) }
            .sumOf { it[it.size / 2] }
        println("Part 1: $result")

        val result2 = updates.filter { ! valid(rules, it) }
            .map { sort(rules, it) }
            //.onEach { println(it) }
            .sumOf { it[it.size / 2] }
        println("Part 2: $result2")
    }
}

private fun sort(rules: Map<Int, Set<Int>>, update: List<Int>): List<Int> {
    return update.sortedWith { o1, o2 ->
        val rule1 = rules[o1]
        val rule2 = rules[o2]
        if (rule1 == null) -1
        else if (rule2 == null) 1
        else if (rule2.contains(o1)) -1
        else if (rule1.contains(o2)) 1
        else 0
    }
}

private fun valid(rules: Map<Int, Set<Int>>, update: List<Int>): Boolean {
    for (i in update.indices.drop(1).reversed()) {
        val u = update[i]
        val before = update[i - 1]
        val rule = rules[u]
        if (rule == null || ! rule.contains(before)) {
            return false
        }
    }
    return true
}

