package kotlinmudv2.action

import kotlinmudv2.item.Item
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.room.RoomService
import kotlinmudv2.socket.Client

class ContextService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val actions: List<Action>,
) {
    companion object {
        fun matchesInput(body: String, needle: String): Boolean {
            return body.split(" ").filter { it.length > 1 }.find {
                it.startsWith(needle)
            } != null
        }
    }

    fun findActionForInput(client: Client, input: String): ActionWithContext? {
        val parts = input.split(" ")
        val context = mutableMapOf<Int, Any>()
        return actions.find { action ->
            var i = 0
            action.syntax.map SyntaxFind@{ syntax ->
                if (i >= parts.size) {
                    return@SyntaxFind false
                }
                val index = i
                i++
                when (syntax) {
                    Syntax.Command -> action.command.value.startsWith(parts[0])
                    Syntax.MobInRoom -> {
                        val mobName = parts[index]
                        findMobInRoom(client.mob!!.roomId, mobName)?.let {
                            context[index] = it
                            true
                        } ?: false
                    }
                    Syntax.ItemInInventory -> {
                        val itemName = parts[index]
                        findItemInInventory(client.mob!!.items, itemName)?.let {
                            context[index] = it
                            true
                        } ?: false
                    }
                    Syntax.ItemInMobInventoryInRoom -> {
                        val itemName = parts[index]
                        mobService.getMobsForRoom(client.mob!!.roomId).find { mob ->
                            mob.items.find { matchesInput(it.brief, itemName) }?.also { item ->
                                context[index] = item
                            } != null
                        }?.let { true } ?: false
                    }
                    Syntax.EquippedItem -> {
                        val itemName = parts[index]
                        findItemInInventory(client.mob!!.equipped, itemName)?.let {
                            context[index] = it
                            true
                        } ?: false
                    }
                    Syntax.ItemInRoom -> {
                        val itemName = parts[index]
                        findItemInRoom(client.mob!!.roomId, itemName)?.let {
                            context[index] = it
                            true
                        } ?: false
                    }
                    Syntax.FreeForm -> {
                        context[index] = parts.drop(index).joinToString(" ")
                        true
                    }
                }
            }.filter { it }.size == action.syntax.size
        }?.let {
            ActionWithContext(it, context)
        }
    }

    private fun findItemInInventory(items: List<Item>, input: String): Item? {
        return items.find {
            matchesInput(it.brief, input)
        }
    }

    private fun findItemInRoom(roomId: Int, input: String): Item? {
        return roomService.getRoom(roomId)?.items?.find {
            matchesInput(it.brief, input)
        }
    }

    private fun findMobInRoom(roomId: Int, input: String): Mob? {
        return mobService.getMobsForRoom(roomId).find {
            matchesInput(it.brief, input)
        }
    }
}
