package kotlinmudv2.action

import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.Mob

class Action(
    val command: Command,
    val syntax: List<Syntax>,
    val dispositions: List<Disposition>,
    val execute: (actionService: ActionService, mob: Mob, context: Map<Int, Any>, input: String) -> Response,
)
