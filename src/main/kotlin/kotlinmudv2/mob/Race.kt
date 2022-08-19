package kotlinmudv2.mob

import kotlinmudv2.game.Attribute

class Race(
    val type: RaceType,
    val attributes: Map<Attribute, Int>,
    val size: RaceSize,
)
