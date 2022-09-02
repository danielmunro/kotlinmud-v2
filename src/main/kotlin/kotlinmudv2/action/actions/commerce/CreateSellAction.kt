package kotlinmudv2.action.actions.commerce

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.mob.MobFlag
import kotlinmudv2.mob.alertDisposition

fun createSellAction(): Action {
    return Action(
        Command.Sell,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { request ->
        val item = request.context[1] as Item
        val shopkeeper = request.getMobsInRoom().find {
            it.flags.contains(MobFlag.Shopkeeper)
        } ?: return@Action request.respondError("there are no shopkeepers here")
        if (item.flags.contains(ItemFlag.ShopInventory)) {
            return@Action request.respondError("they aren't interested in that")
        }
        if (item.value > shopkeeper.coins) {
            return@Action request.respondError("they cannot afford that")
        }
        request.mob.items.remove(item)
        shopkeeper.items.add(item)
        shopkeeper.coins -= item.value
        request.mob.coins += item.value
        request.respondToRoom(
            "you sell $item to $shopkeeper for ${item.value} coins",
            "${request.mob} sells $item to $shopkeeper",
        )
    }
}
