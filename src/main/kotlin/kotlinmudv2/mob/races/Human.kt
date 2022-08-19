package kotlinmudv2.mob.races

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Race
import kotlinmudv2.mob.RaceSize
import kotlinmudv2.mob.RaceType

fun createHumanRace(): Race {
    return Race(
        RaceType.Human,
        mapOf(
            Pair(Attribute.Str, 15),
            Pair(Attribute.Int, 15),
            Pair(Attribute.Wis, 15),
            Pair(Attribute.Dex, 15),
            Pair(Attribute.Con, 15),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 1),
        ),
        RaceSize.Medium,
    )
}
