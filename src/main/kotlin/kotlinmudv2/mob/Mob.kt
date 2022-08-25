package kotlinmudv2.mob

import kotlinmudv2.game.Affect
import kotlinmudv2.game.Attribute
import kotlinmudv2.game.DamageType
import kotlinmudv2.item.Item
import kotlinmudv2.item.Position
import kotlinmudv2.skill.SkillName

open class Mob(
    val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val race: Race,
    var level: Int,
    val items: MutableList<Item>,
    val equipped: MutableList<Item>,
    val attributes: MutableMap<Attribute, Int>,
    val affects: MutableList<Affect>,
    var hp: Int,
    var mana: Int,
    var moves: Int,
    var roomId: Int,
    var coins: Int,
    var disposition: Disposition,
    val flags: List<MobFlag>,
    val skills: MutableMap<SkillName, Int>,
) {
    @Transient var target: Mob? = null

    fun calc(attribute: Attribute): Int {
        return (attributes[attribute] ?: 0) +
            (race.attributes[attribute] ?: 0) +
            (equipped.fold(0) { _, item -> item.attributes[attribute] ?: 0 })
    }

    fun damageType(): DamageType {
        return equipped.find { it.position == Position.Weapon }?.damageType ?: DamageType.Bash
    }

    fun getHealthIndication(): String {
        val amount = hp.toDouble() / calc(Attribute.Hp).toDouble()
        return when {
            amount == 1.0 -> "$name is in excellent condition."
            amount > 0.9 -> "$name has a few scratches."
            amount > 0.75 -> "$name has some small wounds and bruises."
            amount > 0.5 -> "$name has quite a few wounds."
            amount > 0.3 -> "$name has some big nasty wounds and scratches."
            amount > 0.15 -> "$name looks pretty hurt."
            else -> "$name is in awful condition."
        }
    }

    fun regen() {
        val maxHp = calc(Attribute.Hp)
        val hpGain = maxHp / 3
        hp = (hp + hpGain).coerceAtMost(maxHp)
        val maxMana = calc(Attribute.Mana)
        val manaGain = maxMana / 3
        mana = (mana + manaGain).coerceAtMost(maxMana)
        val maxMoves = calc(Attribute.Moves)
        val movesGain = maxMoves / 3
        moves = (moves + movesGain).coerceAtMost(maxMoves)
    }
}
