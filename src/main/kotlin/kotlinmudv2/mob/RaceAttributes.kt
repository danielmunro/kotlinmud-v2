package kotlinmudv2.mob

import kotlinmudv2.game.Attribute

val raceAttributes = mapOf(
    Pair(
        Race.Orc,
        mapOf(
            Pair(Attribute.Str, 20),
            Pair(Attribute.Int, 9),
            Pair(Attribute.Wis, 10),
            Pair(Attribute.Dex, 12),
            Pair(Attribute.Con, 18),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 2),
        ),
    ),
    Pair(
        Race.Human,
        mapOf(
            Pair(Attribute.Str, 15),
            Pair(Attribute.Int, 15),
            Pair(Attribute.Wis, 15),
            Pair(Attribute.Dex, 15),
            Pair(Attribute.Con, 15),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 1),
        ),
    ),
    Pair(
        Race.Elf,
        mapOf(
            Pair(Attribute.Str, 10),
            Pair(Attribute.Int, 19),
            Pair(Attribute.Wis, 19),
            Pair(Attribute.Dex, 16),
            Pair(Attribute.Con, 10),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 1),
        ),
    ),
    Pair(
        Race.Halfling,
        mapOf(
            Pair(Attribute.Str, 14),
            Pair(Attribute.Int, 16),
            Pair(Attribute.Wis, 16),
            Pair(Attribute.Dex, 16),
            Pair(Attribute.Con, 13),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 1),
        ),
    ),
    Pair(
        Race.Dwarf,
        mapOf(
            Pair(Attribute.Str, 17),
            Pair(Attribute.Int, 12),
            Pair(Attribute.Wis, 17),
            Pair(Attribute.Dex, 11),
            Pair(Attribute.Con, 19),
            Pair(Attribute.Hit, 1),
            Pair(Attribute.Dam, 2),
        ),
    ),
)