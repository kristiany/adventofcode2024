import kotlin.math.abs

fun main() {
    forLinesIn("day16/input.txt") { file ->
        val map = file.toList()
        val ways = HashSet<Pair<Int, Int>>()
        var start = 0 to 0
        var end = 0 to 0
        for (y in map.indices) {
            for (x in map[0].indices) {
                if (map[y][x] == '.') {
                    ways.add(x to y)
                }
                else if (map[y][x] == 'E') {
                    end = x to y
                    ways.add(x to y)
                }
                else if (map[y][x] == 'S') {
                    start = x to y
                    ways.add(x to y)
                }
            }
        }

        /*
             N 0
          W 3   E 1
             S 2
         */

        val (score, places) = findScore(ways, start, end)
        println("Part 1: $score")
        println("Part 2: $places")

    }
}

data class Node(val dir: Int, val pos: Pair<Int, Int>)

fun turn(dir: Int, target: Int): Pair<Int, Int> {
    if (target == dir) {
        return target to 0
    }
    // Special-case, to 0-3 or 3-0 turn
    if (abs(target - dir) > 2) {
        return target to 1000
    }
    if (target < dir) {
        return target to (dir - target) * 1000
    }
    return target to (target - dir) * 1000
}

// inspired by the very neat code
// https://www.reddit.com/r/adventofcode/comments/1hfboft/comment/m2bcfmq/
// https://topaz.github.io/paste/#XQAAAQACAwAAAAAAAAAzHIoib6pXbueH4X9F244lVRDcOZab5q1+VXY/ex42qR7D+RJIsq5/YCi6YHJak4tBC2I+oV44GGsk3yILWGnOyXBRhMTGj1iPohCpnraI7cOVIQMgn6rgSzIs8ivVrbZy5UZbCRK5ynh8s/ewf4aWP/ziHGlRvx+evmR5c+dJCcg4aXptpJ4wX011RpSp9CrrqxfG6AHLLvuWe3uAsiz3BMUMAxoduis7+KevEU1N9ZpXOg+AllGx9HrV+/PBI/U7z4HjfpO5j1HdlhNfEp3Qlhydf4IQ/6nQvX39jWc8fG7LXD/YiMj89zdaG/93MmfrJX7dGkxO5W/kKqZqO/tNiX4gHNh8VgWnSZKMZFStlzAbhBBjgAAhbcxSnU9IFqa2T3RwFdPZVnL0q/xI7M4vq/vbdJo4mVGhuuAZwcCRCk7mwXcJxfVxZDgokb7Njdezsi/RwHybpdPs9vYGGixqU7aEfM+AJGtAU9ALoQN+8xKCaeOkuee7lSEd2Qx6qmuA9mcR7JX34lwmrJd0f8PrWWIjtAYYZP787Tg8B22iFBM02EmP5mt5A+hB7aSrldDMh2mb91VXFf/ztA22
private data class Todo(val score: Int, val pos: Pair<Int, Int>, val dir: Int, val path: List<Pair<Int, Int>>)

private fun findScore(ways: Set<Pair<Int, Int>>,
             start: Pair<Int, Int>,
             end: Pair<Int, Int>): Pair<Int, Int> {
    var best = Int.MAX_VALUE
    val seen = HashSet<Pair<Int, Int>>()
    val todo = ArrayList<Todo>(listOf(Todo(0, start, 1, listOf(start))))
    val dist = HashMap<Node, Int>()
    while (todo.isNotEmpty()) {
        val t = todo.removeFirst()
        //println("todo $t")
        if (t.score > dist.getOrDefault(Node(t.dir, t.pos), Int.MAX_VALUE))
            continue

        dist[Node(t.dir, t.pos)] = t.score
        if (t.pos == end && t.score <= best) {
            seen.addAll(t.path)
            best = t.score
        }

        for (neighbor in listOf(
            Node(1, t.pos.first + 1 to t.pos.second),
            Node(3, t.pos.first - 1 to t.pos.second),
            Node(2, t.pos.first to t.pos.second + 1),
            Node(0, t.pos.first to t.pos.second - 1),
        // removing non-ways, and not going backwards
        ).filter { ways.contains(it.pos) && it.dir != (t.dir + 2).mod(4) }) {
            val turn = turn(t.dir, neighbor.dir)
            val path = ArrayList(t.path)
            path.add(neighbor.pos)
            todo.add(Todo(t.score + turn.second + 1, neighbor.pos, turn.first, path))
            todo.sortBy { it.score }
        }
    }
    return best to seen.size
}