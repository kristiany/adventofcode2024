import kotlin.math.pow

fun main() {
    forLinesIn("day17/input.txt") { file ->
        val input = file.toList()
        val A = input.filter { it.contains("A:") }.map { it.split(": ")[1].toLong() }.first()
        val B = input.filter { it.contains("B:") }.map { it.split(": ")[1].toLong() }.first()
        val C = input.filter { it.contains("C:") }.map { it.split(": ")[1].toLong() }.first()
        val prg = input.filter { it.contains("Program:") }
            .map { it.split(": ")[1].split(",").map { it.toInt() }.toList() }
            .first()

        println("A: $A, B: $B, C: $C")
        println(prg)

        val output = runPrg(A, B, C, prg)

        println("Part 1: ${output.joinToString(",")}")

        var step = 10000000L
        var start = 90000000000000L // manually found span to contain 16 digits
        var end =  109019930332929L // found one here, but it's too big
        var digitsCorrect = 0
        search@ while (step > 0 && start != end) {
            println("$start .. $end Step $step")
            for (i in start..end step step) {
                val output2 = runPrg(i, B, C, prg)
                if (output2.size != 16) {
                    println("Size is ${output2.size}, continue")
                    break
                }
                //println("i $i -> $output2")
                if (prg == output2) {
                    println(prg)
                    println(output2)
                    println("Part 2: $i")
                    break@search
                }
                var outputCount = 0
                for (indx in output2.indices.reversed()) {
                    if (output2[indx] == prg[indx]) {
                        outputCount++
                    }
                    else break
                }
                if (outputCount > digitsCorrect) {
                    start = i - step
                    //println("  Output count $outputCount greater than found prev $digitsCorrect, storing new start $start")
                    digitsCorrect = outputCount
                }
                else if (outputCount < digitsCorrect && digitsCorrect - outputCount > 3) {
                    end = i
                    //println("  Output count $outputCount less than found prev $digitsCorrect, storing new end $end")
                    break
                }
            }
            step /= 10
        }
    }
}

fun runPrg(ainit: Long, binit: Long, cinit: Long, prg: List<Int>): List<Int> {
    val output = ArrayList<Int>()
    var pointer = 0
    var a = ainit
    var b = binit
    var c = cinit
    while (pointer < prg.size) {
        val ins = prg[pointer]
        val literalOperand = prg[pointer + 1].toLong()
        val comboOperand = if ((0..3).contains(literalOperand)) literalOperand
            else if (literalOperand == 4L) a
            else if (literalOperand == 5L) b
            else c
        var jump = false
        when (ins) {
            0 -> {
                //println("0: replacing a $a with 2^$comboOperand = ${2.0.pow(comboOperand.toDouble()).toInt()}")
                a = a / 2.0.pow(comboOperand.toDouble()).toLong()
            }
            1 -> {
                //println("1: replacing b $b with b xor $literalOperand = ${b xor literalOperand}")
                b = b xor literalOperand
            }
            2 -> {
                //println("2: replacing b $b with combo $comboOperand mod 8 = ${comboOperand.mod(8)}")
                b = comboOperand.mod(8L)
            }
            3 -> {
                if (a != 0L) {
                    //println("3: jumping from pointer $pointer to $literalOperand")
                    pointer = literalOperand.toInt()
                    jump = true
                }
                else {
                    //println("3: a == 0, no jump")
                }
            }
            4 -> {
                //println("4: replacing b $b with b xor $c = ${b xor c}")
                b = b xor c  // Ignoring operand
            }
            5 -> {
                //println("5:  output combo $comboOperand mod 8 = ${comboOperand.mod(8)}")
                output.add(comboOperand.mod(8))
            }
            6 -> {
                //println("6: replacing b $b with 2^$comboOperand = ${2.0.pow(comboOperand.toDouble()).toInt()}")
                b = a / 2.0.pow(comboOperand.toDouble()).toLong()
            }
            7 -> {
                //println("7: replacing c $c with 2^$comboOperand = ${2.0.pow(comboOperand.toDouble()).toInt()}")
                c = a / 2.0.pow(comboOperand.toDouble()).toLong()
            }
        }
        if (! jump) pointer += 2
    }
    return output
}

