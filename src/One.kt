import kotlin.math.abs

fun main() {
    forLinesIn("day01/input.txt") { row ->
        val input = row.toList()
        val regex = "\\s+".toRegex()
        val list1 = input.map { it.split(regex)[0].toInt() }.sorted()
        val list2 = input.map { it.split(regex)[1].toInt() }.sorted()
        val result = list1.mapIndexed { index, nr -> abs(list2[index] - nr)  }.sum()
        println("Part 1: $result")

        val result2 = list1.map { nr -> nr * list2.filter { it == nr }.count() }.sum()
        println("Part 2: $result2")
    }
}

