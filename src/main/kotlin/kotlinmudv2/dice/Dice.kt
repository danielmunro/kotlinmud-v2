package kotlinmudv2.dice

import kotlin.random.Random

fun d20(): Int {
    return Random.nextInt(1, 21)
}

fun d4(): Int {
    return Random.nextInt(1, 5)
}

fun diceFromString(dice: String): Int {
    val (rolls, sides, bonus) = dice.split("d", "+").map { it.toInt() }
    var amount = bonus
    if (rolls > 0) {
        for (i in 0..rolls) {
            amount += Random.nextInt(sides) + 1
        }
    }
    return amount
}
