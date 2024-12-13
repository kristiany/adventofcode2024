import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.round

fun main() {
    forLinesIn("day13/input.txt") { file ->
        val machines = ArrayList<Machine>()
        var it = file.iterator()
        while (it.hasNext()) {
            val a = parseButton(it.next())
            val b = parseButton(it.next())
            val p = parsePrize(it.next())
            machines.add(Machine(p, a, b))
            if (it.hasNext()) it.next() // Skipping newline
        }
        println(machines)
        /*
            Solving with an equation system
            1. Axa + Bxb = xp
            2. Aya + Byb = yp

            A = (xp - Bxb) / xa

                 yp - yaxp / xa
            B = ------------------
                 -yaxb / xa + yb

         */
        val total = machines.mapNotNull { getScore(it) }.sum()
        println("Part 1: $total")

        val total2 = machines.mapNotNull { getScore2(it) }.sum()
        println("Part 2: $total2")
    }
}

private fun getScore(m: Machine): Long? {
    val xp = m.prize.first.toLong()
    val yp = m.prize.second.toLong()
    val xa = m.adelta.first.toDouble() // Division precision
    val ya = m.adelta.second
    val xb = m.bdelta.first
    val yb = m.bdelta.second
    val b = (yp - (ya * xp) / xa) / ((-ya * xb) / xa + yb)
    val a = (xp - b * xb) / xa
    if (probablyNoDecimals(a) && probablyNoDecimals(b)) {
        return 3L * round(a).toLong() + round(b).toLong()
    }
    return null
}

// Could probably merge these two 👆🏼👇🏼but I don't have the energy today

private fun getScore2(m: Machine): Long? {
    val xp = BigDecimal(m.prize.first.toLong() + 10000000000000L).setScale(12)
    val yp = BigDecimal(m.prize.second.toLong() + 10000000000000L).setScale(12)
    val xa = BigDecimal(m.adelta.first).setScale(12)
    val ya = BigDecimal(m.adelta.second).setScale(12)
    val xb = BigDecimal(m.bdelta.first).setScale(12)
    val yb = BigDecimal(m.bdelta.second).setScale(12)
    val b = (yp - (ya * xp) / xa) / ((-ya * xb) / xa + yb)
    val a = (xp - b * xb) / xa
    if (probablyNoDecimals2(a) && probablyNoDecimals2(b)) {
        return 3L * a.setScale(0, RoundingMode.HALF_UP).toLong() +
                b.setScale(0, RoundingMode.HALF_UP).toLong()
    }
    return null
}

private fun probablyNoDecimals(nr: Double): Boolean {
    return abs(nr - (round(nr).toLong())) < 0.000001
}

private fun probablyNoDecimals2(nr: BigDecimal): Boolean {
    val integerPart = nr.setScale(0, RoundingMode.HALF_EVEN)
    val diff = nr.subtract(integerPart)
    return diff.abs() < BigDecimal(0.000001)
}

private data class Machine(val prize: Pair<Int, Int>, val adelta: Pair<Int, Int>, val bdelta: Pair<Int, Int>)

private fun parseButton(input: String): Pair<Int, Int> {
    val coords = input.split(":")[1].split(",")
    val xstr = coords[0].split("+")[1]
    val ystr = coords[1].split("+")[1]
    return Pair(xstr.toInt(), ystr.toInt())
}

private fun parsePrize(input: String): Pair<Int, Int> {
    val coords = input.split(":")[1].split(",")
    val xstr = coords[0].split("=")[1]
    val ystr = coords[1].split("=")[1]
    return Pair(xstr.toInt(), ystr.toInt())
}
