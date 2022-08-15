package kotlinmudv2.room

enum class Direction {
    North,
    South,
    East,
    West,
    Up,
    Down,
}

fun opposite(direction: Direction): Direction {
    return when (direction) {
        Direction.North -> Direction.South
        Direction.South -> Direction.North
        Direction.East -> Direction.West
        Direction.West -> Direction.East
        Direction.Up -> Direction.Down
        Direction.Down -> Direction.Up
    }
}
