package kotlinmudv2.game

import com.google.gson.GsonBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobEntity
import kotlinmudv2.mob.MobService
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.room.RoomService
import org.jetbrains.exposed.sql.transactions.transaction

class WebServerService(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val roomService: RoomService,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun start() {
        embeddedServer(Netty, port = 8000) {
            routing {
                get("/") {
                    call.respondText("hello world")
                }
                get("/room/{roomId}") {
                    val roomId = call.parameters["roomId"]!!.toInt()
                    transaction { RoomEntity.findById(roomId) }?.let {
                        call.respondText(
                            gson.toJson(
                                roomService.mapRoom(it)
                            )
                        )
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
                post("/room") {
                    val model = gson.fromJson(call.receiveText(), Room::class.java)
                    val entity = transaction {
                        RoomEntity.new {
                            name = model.name
                            description = model.description
                            northId = model.northId
                            southId = model.southId
                            eastId = model.eastId
                            westId = model.westId
                            upId = model.upId
                            downId = model.downId
                        }
                    }
                    // reciprocal connections
                    entity.northId?.let {
                        roomService.connectRooms(it, entity, Direction.South)
                    }
                    entity.southId?.let {
                        roomService.connectRooms(it, entity, Direction.North)
                    }
                    entity.eastId?.let {
                        roomService.connectRooms(it, entity, Direction.West)
                    }
                    entity.westId?.let {
                        roomService.connectRooms(it, entity, Direction.East)
                    }
                    entity.upId?.let {
                        roomService.connectRooms(it, entity, Direction.Down)
                    }
                    entity.downId?.let {
                        roomService.connectRooms(it, entity, Direction.Up)
                    }
                    call.respond(HttpStatusCode.Created)
                }
                get("/mob/{mobId}") {
                    val mobId = call.parameters["mobId"]!!.toInt()
                    transaction { MobEntity.findById(mobId) }?.let {
                        call.respondText(
                            gson.toJson(
                                mobService.mapMob(it)
                            )
                        )
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
                post("/mob") {
                    val model = gson.fromJson(call.receiveText(), Mob::class.java)
                    val entity = transaction {
                        MobEntity.new {
                            name = model.name
                            description = model.description
                            brief = model.brief
                            roomId = model.roomId
                            hp = model.hp
                            maxHp = model.hp
                            mana = model.mana
                            maxMana = model.mana
                            moves = model.moves
                            maxMoves = model.moves
                        }
                    }
                    call.respondText(gson.toJson(mobService.mapMob(entity)), null, HttpStatusCode.Created)
                }
                get("/item/{itemId}") {
                }
                post("/item") {

                }
            }
        }.start(wait = false)
    }
}
