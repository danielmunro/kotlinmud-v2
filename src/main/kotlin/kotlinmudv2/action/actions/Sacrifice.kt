package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.PlayerMob

fun createSacrificeAction(): Action {
    return Action(
        Command.Sacrifice,
        listOf(Syntax.Command, Syntax.ItemInRoom),
        listOf(Disposition.Standing),
    ) { request ->
        val item = request.context[1] as Item
        val amount = (item.value / 100).coerceAtLeast(1).coerceAtMost(200)
        (request.mob as PlayerMob).coins += amount
        request.respondToRoom(
            "Mojo gives you $amount silver for your sacrifice.",
            "${request.mob} sacrifices $item to its deity.",
        )
    }
}
