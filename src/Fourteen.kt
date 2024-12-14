import kotlin.math.abs

fun main() {
    forLinesIn("day14/input.txt") { file ->
        val robotsStart = file.map {
            val parts = it.split(" ")
            val robx = parts[0].split(",")[0].split("=")[1].toInt()
            val roby = parts[0].split(",")[1].toInt()
            val velx = parts[1].split(",")[0].split("=")[1].toInt()
            val vely = parts[1].split(",")[1].toInt()
            Rob(robx to roby, velx to vely)
        }.toList()

        // For real input
        val w = 101
        val h = 103

        //For test
        //val w = 11
        //val h = 7

        //println(robots)

        val robots = robotsStart.map { it.move(w, h, 100) }
        //println(robots)

        val midx = w / 2
        val midy = h / 2
        val q1 = robots.filter { it.pos.first < midx && it.pos.second < midy }
            .count()
        val q2 = robots.filter { it.pos.first > midx && it.pos.second < midy }
            .count()
        val q3 = robots.filter { it.pos.first < midx && it.pos.second > midy }
            .count()
        val q4 = robots.filter { it.pos.first > midx && it.pos.second > midy }
            .count()
        val factor = q1 * q2 * q3 * q4

        println("Part 1: $factor")

        for (t in 1..10000) {
            val robots2 = robotsStart.map { it.move(w, h, t) }
            val robotPos = robots2.map { it.pos }.toSet()

            if (robotPos.size == robots2.size) {
                draw(w, h, robotPos)
                print("Part 2: $t")
                break
            }
        }
    }
}

private fun draw(
    w: Int,
    h: Int,
    robotPos: Set<Pair<Int, Int>>
) {
    for (y in 0..<h) {
        for (x in 0..<w) {
            if (robotPos.contains(x to y)) {
                print("*")
            } else {
                print(" ")
            }
        }
        println()
    }
}

private data class Rob(val pos: Pair<Int, Int>, val vel: Pair<Int, Int>) {
    fun move(w: Int, h: Int, time: Int): Rob {
        return Rob((pos.first + vel.first * time).mod(w) to (pos.second + vel.second * time).mod(h),
            vel)
    }
}