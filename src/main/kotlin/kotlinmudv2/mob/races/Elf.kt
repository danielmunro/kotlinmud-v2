package kotlinmudv2.mob.races

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Race
import kotlinmudv2.mob.RaceSize
import kotlinmudv2.mob.RaceType

fun createElfRace(): Race {
    return Race(
        RaceType.Elf,
        mapOf(
            Pair(Attribute.Str, 10),
            Pair(Attribute.Int, 19),
            Pair(Attribute.Wis, 19),
            Pair(Attribute.Dex, 16),
            Pair(Attribute.Con, 10),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 1),
        ),
        RaceSize.Small,
    )
}
