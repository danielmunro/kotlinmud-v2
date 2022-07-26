package kotlinmudv2.action

enum class Command(val value: String) {
    // information
    Look("look"),

    // potions
    Quaff("quaff"),

    // social
    Say("say"),
    Gossip("gossip"),

    // fighting
    Kill("kill"),
    Flee("flee"),

    // movement
    North("north"),
    South("south"),
    East("east"),
    West("west"),
    Up("up"),
    Down("down"),
}
