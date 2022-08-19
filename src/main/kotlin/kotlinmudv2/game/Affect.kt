package kotlinmudv2.game

class Affect(
    val type: AffectType,
    var timeout: Int,
    val attributes: Map<Attribute, Int> = mapOf(),
)
