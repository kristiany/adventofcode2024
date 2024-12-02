import kotlin.math.abs

fun main() {
    forLinesIn("day02/input.txt") { lines ->
        val reports = lines.toList()
            .map { it.split(" ").map { it.toInt() } }

        val result = reports.map { report -> diff(report) }
            .map { diff -> safe(diff) }
        println("Part 1: ${result.count { it }}")

        val result2 = reports
            .map { report ->
                for (i in report.indices) {
                    val reducedReport = report.filterIndexed { index, _ -> index != i }
                    val reducedDiff = diff(reducedReport)
                    if (safe(reducedDiff)) {
                        return@map true
                    }
                }
                return@map false
            }
        println("Part 2: ${result2.count { it }}")
    }

}

fun diff(report: List<Int>): List<Int> {
    val result = ArrayList<Int>()
    for (i in 1 ..< report.size) {
        result.add(report[i] - report[i - 1])
    }
    return result
}

fun safe(diff: List<Int>): Boolean {
    return (diff.all { it > 0 } || diff.all { it < 0 })
            && diff.all { abs(it) in 1..3 }
}
