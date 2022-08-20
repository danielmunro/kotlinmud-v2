package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.errorResponse
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.alertDisposition

fun createQuaffAction(): Action {
    return Action(
        Command.Quaff,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { _, mob, context, _ ->
        (context[1] as Item).let {
            return@Action if (it.itemType != ItemType.Potion) {
                errorResponse(
                    mob,
                    "that's not a potion",
                )
            } else {
                mob.items.remove(it)
                Response(mob, "you quaff ${it.name}")
            }
        }
    }
}
