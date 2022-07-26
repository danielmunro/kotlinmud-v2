package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType

fun createQuaffAction(): Action {
    return Action(
        Command.Quaff,
        listOf(Syntax.Command, Syntax.ItemInInventory),
    ) { _, mob, context, _ ->
        (context[1] as Item).let {
            return@Action if (it.itemType != ItemType.Potion) {
                Response(
                    mob,
                    "that's not a potion",
                    ActionStatus.Error,
                )
            } else {
                mob.items.remove(it)
                Response(mob, "you quaff ${it.name}")
            }
        }
    }
}
