package kotlinmudv2.action

import kotlinmudv2.mob.Disposition

class Action(
    val command: Command,
    val syntax: List<Syntax>,
    val dispositions: List<Disposition>,
    val execute: (request: Request) -> Response,
)
