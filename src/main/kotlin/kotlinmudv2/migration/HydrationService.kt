package kotlinmudv2.migration

import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemType
import kotlinmudv2.item.Material
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.MobEntity
import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction

class HydrationService(
    private val roomModels: MutableMap<Int, Map<String, String?>>,
    private val mobModels: MutableMap<Int, Map<String, String>>,
    private val itemModels: MutableMap<Int, Map<String, String>>,
    private val mobResets: MutableMap<Int, MutableList<MobReset>>,
    private val itemRoomResets: MutableMap<Int, MutableList<ItemRoomReset>>,
    private val itemMobInventoryResets: MutableMap<Int, MutableList<ItemMobReset>>,
    private val itemMobEquippedResets: MutableMap<Int, MutableList<ItemMobReset>>,
) {
    private var lastRoom: RoomEntity? = null
    private val unknownMaterials = mutableSetOf<String>()

    companion object {
        fun mapItemType(type: String): String {
            return when (type) {
                "Warp_stone" -> "WarpStone"
                "Spell_page" -> "SpellPage"
                "Room_key" -> "Key"
                "Trap_part" -> "TrapPart"
                "Item_part" -> "ItemPart"
                "Npc_corpse" -> "NpcCorpse"
                else -> {
                    try {
                        ItemType.valueOf(type)
                        return type
                    } catch (e: IllegalArgumentException) {
                        if (type != "") {
                            println("unknown type: $type")
                        }
                        return "Indeterminate"
                    }
                }
            }
        }
    }

    fun mapMaterial(material: String): String {
        return when (material) {
            "Bird" -> "Flesh"
            "Cherry wood" -> "Wood"
            "Stuff" -> "Unknown"
            "Water" -> "Liquid"
            "Calcium rock stalagmite" -> "Rock"
            "Amethyst rock mineral" -> "Rock"
            "Waterfall" -> "Liquid"
            "Oldstyle" -> "Unknown"
            "Vegtable" -> "Vegetable"
            "Fabric" -> "Cloth"
            "Faerie" -> "Organic"
            "Nature spring" -> "Water"
            "Runts" -> "Organic"
            "Yinn" -> "Organic"
            "Kender" -> "Organic"
            "Silver and gold" -> "Alloy"
            "Silver and Gold" -> "Alloy"
            "Satan" -> "Evil"
            "Crud" -> "Trash"
            "Dog hair" -> "Fur"
            "Persperation" -> "Water"
            "Finnish Vodka" -> "Alcohol"
            "Solid gold" -> "Gold"
            "Whicker" -> "Wicker"
            "Rosewood" -> "Wood"
            "Nothing" -> "Unknown"
            "Steam" -> "Water"
            "Pasta" -> "Food"
            "Egg" -> "Food"
            "None" -> "Unknown"
            "Porcelin" -> "Porcelain"
            "Denim" -> "Textile"
            "Sex" -> "Unknown"
            "Corpses" -> "Corpse"
            "Animal furs" -> "Fur"
            "Pine needle" -> "Organic"
            "Linen" -> "Textile"
            "Black Dragon Leather" -> "DragonSkin"
            "Feathers" -> "Feather"
            "Splinters and dried blood" -> "Blood"
            "Lapis lazuli" -> "LapisLazuli"
            "Fish" -> "Meat"
            "Red gold" -> "RedGold"
            "Dust" -> "Earth"
            "Larva" -> "Organic"
            "Eel" -> "Organic"
            "Finest cotton" -> "Cotton"
            else -> {
                try {
                    Material.valueOf(material)
                    return material
                } catch (e: IllegalArgumentException) {
                    if (material != "") {
                        unknownMaterials.add(material)
                    }
                    return "Unknown"
                }
            }
        }
    }

    fun hydrate() {
        hydrateMobs()
        hydrateRooms()
        println(unknownMaterials.joinToString("\n"))
    }

    private fun hydrateRooms() {
        roomModels.forEach {
            val id = it.key
            val props = it.value
            lastRoom = transaction {
                RoomEntity.new(id) {
                    name = props["name"]!!.trim()
                    description = props["description"]!!.trim()
                    northId = props["northId"]?.toInt()
                    southId = props["southId"]?.toInt()
                    eastId = props["eastId"]?.toInt()
                    westId = props["westId"]?.toInt()
                    upId = props["upId"]?.toInt()
                    downId = props["downId"]?.toInt()
                }.also { room ->
                    itemRoomResets[id]?.forEach { reset ->
                        itemModels[reset.itemId]?.also { model ->
                            createItemEntityFromModel(model).also { entity ->
                                entity.room = room.id
                            }
                        } ?: println("item model missing ${reset.itemId}")
                    }
                }
            }
        }
    }

    private fun hydrateMobs() {
        mobModels.forEach { (id, props) ->
            mobResets[id]?.forEach {
                val flag3 = props["flags3"]!!.split(" ")

                transaction {
                    MobEntity.new {
                        canonicalId = id
                        name = props["name"]!!.trim()
                        brief = props["brief"]!!.trim()
                        description = props["description"]!!.trim()
                        race = MigrationService.mapRace(props["race"]!!.trim().capitalize())
                        level = flag3[0].toInt()
                        hp = flag3[2]
                        mana = flag3[3]
                        moves = flag3[4]
                        maxInGame = it.maxInGame
                        maxInRoom = it.maxInRoom
                        roomId = it.roomId
                        attributes = mutableMapOf()
                        disposition = Disposition.Standing.toString()
                        affects = mutableMapOf()
                    }.also { mob ->
                        itemMobInventoryResets[id]?.forEach {
                            itemModels[it.itemId]?.also { model ->
                                createItemEntityFromModel(model).also { entity ->
                                    entity.mobInventory = mob.id
                                }
                            } ?: println("item model missing ${it.itemId}")
                        }
                        itemMobEquippedResets[id]?.forEach {
                            itemModels[it.itemId]?.also { model ->
                                createItemEntityFromModel(model).also { entity ->
                                    entity.mobEquipped = mob.id
                                }
                            } ?: println("item model missing ${it.itemId}")
                        }
                    }
                }
            }
        }
    }

    private fun createItemEntityFromModel(model: Map<String, String>): ItemEntity {
        val flag1 = model["flags1"]!!.trim().split(" ")
        val flag3 = model["flags3"]!!.trim().split(" ")
        return ItemEntity.new {
            name = model["name"]!!.trim()
            brief = model["brief"]!!.trim()
            description = model["description"]!!.trim()
            itemType = ItemType.valueOf(mapItemType(flag1[0].capitalize())).toString()
            material = Material.valueOf(mapMaterial(model["material"]!!.trim().capitalize())).toString()
            attributes = mutableMapOf()
            affects = mutableMapOf()
            level = flag3[0].toInt()
            weight = flag3[1].toInt()
            value = flag3[2].toInt()
        }
    }
}
