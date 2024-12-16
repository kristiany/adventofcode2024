fun main() {
    forLinesIn("day15/input.txt") { row ->
        val input = row.toList()
        val boxesStart = HashSet<Pair<Int, Int>>()
        val walls = HashSet<Pair<Int, Int>>()
        var robotStart = 0 to 0
        var movements = ""
        input.forEachIndexed { y, line ->
            if (line.startsWith("#")) {
                line.forEachIndexed { x, c ->
                    if (c == 'O')
                        boxesStart.add(x to y)
                    else if (c == '#')
                        walls.add(x to y)
                    else if (c == '@')
                        robotStart = x to y
                }
            } else if (line.isNotEmpty()) {
                movements += line
            }
        }

        val w = input[0].length
        val h = input.count { it.startsWith("#") }
        var robot = robotStart
        var boxes = HashSet(boxesStart)
        for (move in movements) {
            //println("Move $move")
            when (move) {
                '^' -> robot = moveUp(robot, walls, boxes)
                '>' -> robot = moveRight(robot, walls, boxes)
                'v' -> robot = moveDown(robot, walls, boxes)
                '<' -> robot = moveLeft(robot, walls, boxes)
            }
            //debug(w, h, robot, walls, boxes)
        }
        debug(w, h, robot, walls, boxes.map { Box(it.first, it.second) }.toSet())

        val gps = boxes.sumOf { it.first + it.second * 100 }
        println("Part 1: $gps")

        val walls2 = walls.flatMap {
            setOf(it.first * 2 to it.second, it.first * 2 + 1 to it.second)
        }.toSet()
        val boxes2 = HashSet(boxesStart.map { Box(it.first * 2, it.second) })
        var robot2 = robotStart.first * 2 to robotStart.second
        val w2 = w * 2
        debug(w2, h, robot2, walls2, boxes2, 2)
        for (move in movements) {
            //println("Move $move")
            when (move) {
                '^' -> robot2 = moveUp2(robot2, walls2, boxes2)
                '>' -> robot2 = moveRight2(robot2, walls2, boxes2)
                'v' -> robot2 = moveDown2(robot2, walls2, boxes2)
                '<' -> robot2 = moveLeft2(robot2, walls2, boxes2)
            }
            //debug(w2, h, robot2, walls2, boxes2, 2)
        }
        //debug(w2, h, robot2, walls2, boxes2, 2)
        val gps2 = boxes2.sumOf { it.x + it.y * 100 }
        println("Part 2: $gps2")
    }
}

private fun debug(w: Int, h: Int, robot: Pair<Int, Int>,
          walls: Set<Pair<Int, Int>>,
          boxes: Set<Box>,
          wf: Int = 1) {
    val boxPos = if (wf == 2)
        boxes.flatMap { setOf(it.x to it.y, it.x2() to it.y) }.toSet()
    else
        boxes
    for (y in 0..< h) {
        for (x in 0..< w) {
            if (boxPos.contains(x to y)) {
                print('O')
            }
            else if (walls.contains(x to y)) {
                print('#')
            }
            else if (robot == x to y) {
                print('@')
            }
            else {
                print('.')
            }
        }
        println()
    }
}

private fun moveUp(robot: Pair<Int, Int>,
                   walls: Set<Pair<Int, Int>>,
                   boxes: HashSet<Pair<Int, Int>>): Pair<Int, Int> {
    val x = robot.first
    if (walls.contains(x to robot.second - 1)) {
        return robot
    }
    if (! boxes.contains(x to robot.second - 1)) {
        return x to robot.second - 1
    }
    var yit = robot.second - 1
    while (boxes.contains(x to yit)) {
        --yit
    }
    // All stones are against the wall
    if (walls.contains(x to yit)) {
        return robot
    }
    for (y in yit..< robot.second - 1) {
        boxes.remove(x to y + 1)
        boxes.add(x to y)
    }
    return x to robot.second - 1
}

private fun moveRight(robot: Pair<Int, Int>,
                      walls: Set<Pair<Int, Int>>,
                      boxes: HashSet<Pair<Int, Int>>): Pair<Int, Int> {
    val y = robot.second
    if (walls.contains(robot.first + 1 to y)) {
        return robot
    }
    if (! boxes.contains(robot.first + 1 to y)) {
        return robot.first + 1 to y
    }

    var xit = robot.first + 1
    while (boxes.contains(xit to y)) {
        ++xit
    }
    // All boxes are against the wall
    if (walls.contains(xit to y)) {
        return robot
    }
    for (x in xit downTo robot.first + 2) {
        boxes.remove(x - 1 to y)
        boxes.add(x to y)
    }
    return robot.first + 1 to y
}

private fun moveDown(robot: Pair<Int, Int>,
                     walls: Set<Pair<Int, Int>>,
                     boxes: HashSet<Pair<Int, Int>>): Pair<Int, Int> {
    val x = robot.first
    if (walls.contains(x to robot.second + 1)) {
        return robot
    }
    if (! boxes.contains(x to robot.second + 1)) {
        return x to robot.second + 1
    }
    var yit = robot.second + 1
    while (boxes.contains(x to yit)) {
        ++yit
    }
    // All stones are against the wall
    if (walls.contains(x to yit)) {
        return robot
    }
    for (y in yit downTo robot.second + 2) {
        boxes.remove(x to y - 1)
        boxes.add(x to y)
    }
    return x to robot.second + 1
}

private fun moveLeft(robot: Pair<Int, Int>,
                     walls: Set<Pair<Int, Int>>,
                     boxes: HashSet<Pair<Int, Int>>): Pair<Int, Int> {
    val y = robot.second
    if (walls.contains(robot.first - 1 to y)) {
        return robot
    }
    if (! boxes.contains(robot.first - 1 to y)) {
        return robot.first - 1 to y
    }

    var xit = robot.first - 1
    while (boxes.contains(xit to y)) {
        --xit
    }
    // All boxes are against the wall
    if (walls.contains(xit to y)) {
        return robot
    }
    for (x in xit ..< robot.first - 1) {
        boxes.remove(x + 1 to y)
        boxes.add(x to y)
    }
    return robot.first - 1 to y
}

private fun moveUp2(robot: Pair<Int, Int>,
                   walls: Set<Pair<Int, Int>>,
                   boxes: HashSet<Box>): Pair<Int, Int> {
    val x = robot.first
    if (walls.contains(x to robot.second - 1)) {
        return robot
    }
    if (! (boxes.contains(Box(x, robot.second - 1)) || boxes.contains(Box(x - 1, robot.second - 1)))) {
        return x to robot.second - 1
    }
    var yit = robot.second - 1
    var prev = if (boxes.contains(Box(x, yit)))
            listOf(Box(x, robot.second - 1))
        else
            listOf(Box(x - 1, yit))
    val toMove = ArrayList<Box>(prev)
    do {
        --yit
        val boxCheck = prev.flatMap {
            listOf(
                Box(it.x, yit), // straight on top
                Box(it.x2(), yit), // to the right
                Box(it.x - 1, yit) // to the left
            )
            .filter { boxes.contains(it) }
        }.toList()
        val wallExists = prev.any { walls.contains(it.x to yit) || walls.contains(it.x2() to yit) }
        if (boxCheck.isEmpty() || wallExists) {
            break
        }
        prev = boxCheck
        toMove.addAll(boxCheck)
    } while (boxCheck.isNotEmpty())
    // Any box is against the wall
    if (prev.any { walls.contains(it.x to yit) || walls.contains(it.x2() to yit) }) {
        return robot
    }
    for (box in toMove.reversed()) {
        boxes.remove(box)
        boxes.add(Box(box.x, box.y - 1))
    }
    return x to robot.second - 1
}

private fun moveRight2(robot: Pair<Int, Int>,
                      walls: Set<Pair<Int, Int>>,
                      boxes: HashSet<Box>): Pair<Int, Int> {
    val y = robot.second
    if (walls.contains(robot.first + 1 to y)) {
        return robot
    }
    if (! boxes.contains(Box(robot.first + 1, y))) {
        return robot.first + 1 to y
    }

    var xit = robot.first + 1
    val toMove = ArrayList<Box>()
    while (boxes.contains(Box(xit, y))) {
        toMove.add(Box(xit, y))
        xit += 2
    }
    // All boxes are against the wall
    if (walls.contains(xit to y)) {
        return robot
    }
    for (box in toMove.reversed()) {
        boxes.remove(box)
        boxes.add(Box(box.x + 1, box.y))
    }
    return robot.first + 1 to y
}

private fun moveDown2(robot: Pair<Int, Int>,
                     walls: Set<Pair<Int, Int>>,
                     boxes: HashSet<Box>): Pair<Int, Int> {
    val x = robot.first
    val nexty = robot.second + 1
    if (walls.contains(x to nexty)) {
        return robot
    }
    if (! (boxes.contains(Box(x, nexty)) || boxes.contains(Box(x - 1, nexty)))) {
        return x to nexty
    }
    var yit = nexty
    var prev = if (boxes.contains(Box(x, nexty)))
        listOf(Box(x, nexty))
    else
        listOf(Box(x - 1, nexty))
    val toMove = ArrayList<Box>(prev)
    do {
        ++yit
        val boxCheck = prev.flatMap {
            listOf(
                Box(it.x, yit), // straight on top
                Box(it.x2(), yit), // to the right
                Box(it.x - 1, yit) // to the left
            )
            .filter { boxes.contains(it) }
        }.toList()
        val wallExists = prev.any { walls.contains(it.x to yit) || walls.contains(it.x2() to yit) }
        if (boxCheck.isEmpty() || wallExists) {
            break
        }
        prev = boxCheck
        toMove.addAll(boxCheck)
    } while (boxCheck.isNotEmpty())
    // Any box is against the wall
    if (prev.any { walls.contains(it.x to yit) || walls.contains(it.x2() to yit) }) {
        return robot
    }
    for (box in toMove.reversed()) {
        boxes.remove(box)
        boxes.add(Box(box.x, box.y + 1))
    }
    return x to nexty
}

private fun moveLeft2(robot: Pair<Int, Int>,
                     walls: Set<Pair<Int, Int>>,
                     boxes: HashSet<Box>): Pair<Int, Int> {
    val y = robot.second
    if (walls.contains(robot.first - 1 to y)) {
        return robot
    }
    if (! boxes.contains(Box(robot.first - 2, y))) {
        return robot.first - 1 to y
    }
    val toMove = ArrayList<Box>()
    var xit = robot.first - 2
    while (boxes.contains(Box(xit, y))) {
        toMove.add(Box(xit, y))
        xit -= 2
    }
    xit++ // Move to right part
    // All boxes are against the wall
    if (walls.contains(xit to y)) {
        return robot
    }
    for (box in toMove.reversed()) {
        boxes.remove(box)
        boxes.add(Box(box.x - 1, box.y))
    }
    return robot.first - 1 to y
}

private data class Box(val x: Int, val y: Int) {
    fun x2(): Int {
        return x + 1
    }
}