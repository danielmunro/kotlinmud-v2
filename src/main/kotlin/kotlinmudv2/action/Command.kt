package kotlinmudv2.action

enum class Command(val value: String) {
    // information
    Look("look"),

    // potions
    Quaff("quaff"),

    // items
    Get("get"),
    Sacrifice("sacrifice"),

    // equipment
    Wear("wear"),
    Remove("remove"),

    // shops
    List("list"),
    Buy("buy"),
    Sell("sell"),

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
