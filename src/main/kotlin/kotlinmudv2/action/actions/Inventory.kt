package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.mob.anyDisposition

fun createInventoryAction(): Action {
    return Action(
        Command.Inventory,
        listOf(Syntax.Command),
        anyDisposition(),
    ) { _, mob, _, _ ->
        val inv = mutableMapOf<Int, Int>()
        val itemMap = mutableMapOf<Int, Item>()
        mob.items.forEach {
            if (itemMap[it.id] == null) {
                itemMap[it.id] = it
            }
            if (inv[it.id] == null) {
                inv[it.id] = 0
            }
            inv[it.id] = inv[it.id]!! + 1
        }
        Response(
            mob,
            "your inventory:\n${inv.map {"[${String.format("%1$3s", it.value)}] ${itemMap[it.key]?.name}"}.joinToString("\n")}",
        )
    }
}
