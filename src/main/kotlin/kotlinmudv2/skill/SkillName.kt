package kotlinmudv2.skill

enum class SkillName(val value: String) {
    // warrior
    Bash("bash"),
    DirtKick("dirt kick"),
    Disarm("disarm"),

    // thief
    BackStab("backstab"),
    Hamstring("hamstring"),
    Sneak("sneak"),

    // cleric
    Heal("heal"),
    LayOnHands("lay on hands"),
    Sanctuary("sanctuary"),

    // mage
    MagicMissile("magic missile"),
    Fireball("fireball"),
    Enfeeble("enfeeble"),
}
