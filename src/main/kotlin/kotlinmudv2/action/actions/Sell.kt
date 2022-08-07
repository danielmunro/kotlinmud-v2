package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
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
    ) { actionService, mob, context, _ ->
        val item = context[1] as Item
        val shopkeeper = actionService.getMobsInRoom(mob.roomId).find {
            it.flags.contains(MobFlag.Shopkeeper)
        } ?: return@Action Response(mob, "there are no shopkeepers here")
        if (item.flags.contains(ItemFlag.ShopInventory)) {
            return@Action Response(mob, "they aren't interested in that")
        }
        if (item.value > shopkeeper.coins) {
            return@Action Response(mob, "they cannot afford that")
        }
        mob.items.remove(item)
        shopkeeper.items.add(item)
        shopkeeper.coins -= item.value
        mob.coins += item.value
        Response(
            mob,
            "you sell ${item.name} for ${item.value} coins",
        )
    }
}
