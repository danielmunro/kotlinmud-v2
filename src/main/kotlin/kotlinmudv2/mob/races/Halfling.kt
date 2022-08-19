package kotlinmudv2.mob.races

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Race
import kotlinmudv2.mob.RaceSize
import kotlinmudv2.mob.RaceType

fun createHalflingRace(): Race {
    return Race(
        RaceType.Halfling,
        mapOf(
            Pair(Attribute.Str, 14),
            Pair(Attribute.Int, 16),
            Pair(Attribute.Wis, 16),
            Pair(Attribute.Dex, 16),
            Pair(Attribute.Con, 13),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 1),
        ),
        RaceSize.Small,
    )
}
