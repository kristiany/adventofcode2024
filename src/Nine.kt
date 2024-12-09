fun main() {
    forLinesIn("day09/input.txt") { file ->
        val fs = file.toList()[0].map { it.toString().toInt() }.toList()
        println(fs)
        var pos = 0
        val files = ArrayList<File>()
        val space = ArrayList<Space>()
        for (i in fs.indices) {
            val nr = fs[i]
            if (i % 2 == 0) {
                files.add(File(i / 2, arrayListOf(pos ..< pos + nr)))
            }
            else if (nr > 0) {
                space.add(Space(pos ..< pos + nr))
            }
            pos += nr
        }
        var defragged = defrag(space, files)
        //println(defragged)
        val result = checksum(defragged)
        //debug(defragged)
        println("Part 1: ${result}")

        var defragged2 = defrag2(space, files)
        //println(defragged)
        val result2 = checksum(defragged2)
        //debug(defragged)
        println("Part 2: ${result2}")
    }
}

private fun checksum(defragged: ArrayList<File>): Long {
    val result = defragged.flatMap { f -> f.blocks.flatMap { block -> block.map { f.id * it.toLong() } } }
    return result.sum()
}

private fun debug(defragged: ArrayList<File>) {
    for (i in 0..defragged.maxOf { it.blocks.maxOf { it.last } }) {
        val f = defragged.find { it.blocks.any { it.contains(i) } }
        if (f != null) print(f.id)
        else print(".")
    }
    println()
}

private fun defrag(spaceInput: List<Space>, filesInput: List<File>): ArrayList<File> {
    val space = ArrayList(spaceInput)
    val files = ArrayList(filesInput.map { File(it.id, ArrayList(it.blocks)) })
    var freeSpace = space.removeFirst().blocks
    var fileIndex = files.lastIndex
    while (freeSpace.last - freeSpace.first + 1 > 0 && space.size > 0 && fileIndex >= 0) {
        var file = files[fileIndex]
        if (file.blocks.last().first < freeSpace.first) {
            break
        }
        var blocks = file.popLast()
        val freeSpaceSize = freeSpace.last - freeSpace.first + 1
        val blockSize = blocks.last - blocks.first + 1
        if (freeSpaceSize == blockSize) {
            file.addBlock(freeSpace)
            fileIndex -= 1
            space.add(Space(blocks))
            space.sortBy { it.blocks.first }
            freeSpace = space.removeFirst().blocks
        }
        // Free space is larger
        else if (freeSpaceSize > blockSize) {
            file.addBlock(freeSpace.first..<freeSpace.first + blockSize)
            freeSpace = freeSpace.first + blockSize..freeSpace.last
            fileIndex -= 1
            space.add(Space(blocks))
            space.sortBy { it.blocks.first }
        }
        // Files blocks is larger
        else {
            file.addBlock(freeSpace)
            file.addBlock(blocks.first..blocks.last - freeSpaceSize) // adding back the rest file blocks
            space.add(Space(blocks.last - freeSpaceSize + 1..blocks.last))
            space.sortBy { it.blocks.first }
            freeSpace = space.removeFirst().blocks
        }
    }
    return files
}

private fun defrag2(spaceInput: List<Space>, filesInput: List<File>): ArrayList<File> {
    val space = ArrayList(spaceInput)
    val files = ArrayList(filesInput.map { File(it.id, ArrayList(it.blocks)) })
    for (fileIndex in files.indices.reversed()) {
        val file = files[fileIndex]
        val blocks = file.blocks.last()
        val blockSize = blocks.last - blocks.first + 1
        for (spaceIndex in space.indices) {
            val freeSpace = space[spaceIndex]
            if (freeSpace.blocks.first > blocks.first) {
                break // Already checked all spaces to the left
            }
            val freeSpaceSize = freeSpace.blocks.last - freeSpace.blocks.first + 1
            if (freeSpaceSize == blockSize) {
                file.popLast() // Remove the current space taken
                file.addBlock(freeSpace.blocks)
                space.removeAt(spaceIndex)
                break
            }
            // Free space is larger
            else if (freeSpaceSize > blockSize) {
                file.popLast() // Remove the current space taken
                file.addBlock(freeSpace.blocks.first..<freeSpace.blocks.first + blockSize)
                val freeSpaceRest = freeSpace.blocks.first + blockSize..freeSpace.blocks.last
                space[spaceIndex] = Space(freeSpaceRest)
                break
            }
        }
    }
    return files
}

private data class File(val id: Int, val blocks: ArrayList<IntRange>) {
    fun popLast(): IntRange {
        return blocks.removeLast()
    }

    fun addBlock(block: IntRange) {
        blocks.add(block)
    }
}
private data class Space(val blocks: IntRange)

