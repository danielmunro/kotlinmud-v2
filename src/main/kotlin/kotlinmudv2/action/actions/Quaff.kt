package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.alertDisposition

fun createQuaffAction(): Action {
    return Action(
        Command.Quaff,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { request ->
        (request.context[1] as Item).let {
            return@Action if (it.itemType != ItemType.Potion) {
                request.respondError("that's not a potion")
            } else {
                request.mob.items.remove(it)
                request.respond("you quaff ${it.name}")
            }
        }
    }
}
