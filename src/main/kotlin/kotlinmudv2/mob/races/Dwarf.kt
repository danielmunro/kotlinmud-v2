package kotlinmudv2.mob.races

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Race
import kotlinmudv2.mob.RaceSize
import kotlinmudv2.mob.RaceType

fun createDwarfRace(): Race {
    return Race(
        RaceType.Dwarf,
        mapOf(
            Pair(Attribute.Str, 17),
            Pair(Attribute.Int, 12),
            Pair(Attribute.Wis, 17),
            Pair(Attribute.Dex, 11),
            Pair(Attribute.Con, 19),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 2),
        ),
        RaceSize.Small,
    )
}
