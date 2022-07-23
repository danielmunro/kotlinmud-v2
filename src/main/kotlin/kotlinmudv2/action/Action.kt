package kotlinmudv2.action

import kotlinmudv2.mob.Mob

class Action(
    val command: Command,
    val syntax: List<Syntax>,
    val execute: (actionService: ActionService, mob: Mob, input: String) -> Response,
)
