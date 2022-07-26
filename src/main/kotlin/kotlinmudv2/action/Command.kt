package kotlinmudv2.action

enum class Command(val value: String) {
    // information
    Look("look"),

    // potions
    Quaff("quaff"),

    // social
    Say("say"),
    Gossip("gossip"),

    // movement
    North("north"),
    South("south"),
    East("east"),
    West("west"),
    Up("up"),
    Down("down"),
}
