package kotlinmudv2.action

enum class Command(val value: String) {
    // information
    Look("look"),

    // movement
    North("north"),
    South("south"),
    East("east"),
    West("west"),
    Up("up"),
    Down("down"),
}
