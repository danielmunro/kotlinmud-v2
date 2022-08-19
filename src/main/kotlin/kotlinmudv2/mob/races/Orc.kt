package kotlinmudv2.mob.races

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Race
import kotlinmudv2.mob.RaceSize
import kotlinmudv2.mob.RaceType

fun createOrcRace(): Race {
    return Race(
        RaceType.Orc,
        mapOf(
            Pair(Attribute.Str, 20),
            Pair(Attribute.Int, 9),
            Pair(Attribute.Wis, 10),
            Pair(Attribute.Dex, 12),
            Pair(Attribute.Con, 18),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 2),
        ),
        RaceSize.Large,
    )
}
