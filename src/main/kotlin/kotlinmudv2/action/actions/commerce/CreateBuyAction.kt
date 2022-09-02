package kotlinmudv2.action.actions.commerce

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.mob.MobFlag
import kotlinmudv2.mob.alertDisposition

fun createBuyAction(): Action {
    return Action(
        Command.Buy,
        listOf(Syntax.Command, Syntax.ItemInMobInventoryInRoom),
        alertDisposition(),
    ) { request ->
        val item = request.context[1] as Item
        val shopkeeper = request.getMobsInRoom().find {
            it.flags.contains(MobFlag.Shopkeeper)
        } ?: return@Action request.respondError("there are no shopkeepers here")
        if (item.value > request.mob.coins) {
            return@Action request.respondError("you cannot afford that")
        }
        val toBuy = if (item.flags.contains(ItemFlag.ShopInventory)) {
            request.cloneItem(item)
        } else {
            shopkeeper.items.remove(item)
            item
        }
        request.mob.items.add(toBuy)
        request.mob.coins -= toBuy.value
        shopkeeper.coins += toBuy.value
        request.respondToRoom(
            "you buy $item for ${item.value} gold",
            "${request.mob} buys $item",
        )
    }
}
