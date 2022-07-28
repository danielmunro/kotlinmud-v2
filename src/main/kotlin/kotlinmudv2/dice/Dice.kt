package kotlinmudv2.dice

import kotlin.random.Random

fun d20(): Int {
    return Random.nextInt(1, 21)
}
