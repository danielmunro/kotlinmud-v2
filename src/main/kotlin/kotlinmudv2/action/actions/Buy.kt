package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
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
    ) { actionService, mob, context, _ ->
        val item = context[1] as Item
        val shopkeeper = actionService.getMobsInRoom(mob.roomId).find {
            it.flags.contains(MobFlag.Shopkeeper)
        } ?: return@Action Response(mob, "there are no shopkeepers here")
        if (item.value > mob.coins) {
            return@Action Response(mob, "you cannot afford that")
        }
        val toBuy = if (item.flags.contains(ItemFlag.ShopInventory)) {
            actionService.cloneItem(item)
        } else {
            shopkeeper.items.remove(item)
            item
        }
        mob.items.add(toBuy)
        mob.coins -= toBuy.value
        shopkeeper.coins += toBuy.value
        Response(
            mob,
            "you buy ${item.name} for ${item.value} coins"
        )
    }
}
