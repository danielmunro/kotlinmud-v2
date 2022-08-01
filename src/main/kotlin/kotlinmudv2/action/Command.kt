package kotlinmudv2.action

enum class Command(val value: String) {
    // information
    Look("look"),

    // potions
    Quaff("quaff"),

    // items
    Get("get"),

    // equipment
    Wear("wear"),
    Remove("remove"),

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
    Recall("recall"),
}
