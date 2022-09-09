package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class MoveTest {
    @Test
    fun testHamstringAffectsMovementCost() {
        // setup
        val test = createTestService()
        val destinationId: Int
        test.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            test.startRoom.id,
            Exit(Direction.North, destinationId),
        )

        // given
        test.getPlayerMob().affects.add(Affect(AffectType.Hamstrung, 1))

        // when
        test.handleRequest("north")

        // then
        assertThat(test.getPlayerMob().moves).isEqualTo((test.getPlayerMob().attributes[Attribute.Moves] ?: 0) - 5)
    }

    @Test
    fun testCannotMoveIfOutOfMoves() {
        // setup
        val test = createTestService()
        val destinationId: Int
        test.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            test.startRoom.id,
            Exit(Direction.North, destinationId),
        )

        // given
        test.getPlayerMob().moves = 0

        // when
        val response = test.handleRequest("north")

        // then
        assertThat(response.toActionCreator).isEqualTo("you are too tired to move.")
    }

    @Test
    fun testCanMoveNorth() {
        // setup
        val testService = createTestService()

        // given
        val destinationId: Int
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            testService.startRoom.id,
            Exit(Direction.North, destinationId),
        )

        // when
        val response = testService.handleRequest("north")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: S]\n")
    }

    @Test
    fun testCanMoveSouth() {
        // setup
        val testService = createTestService()

        // given
        val destinationId: Int
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            testService.startRoom.id,
            Exit(Direction.South, destinationId),
        )

        // when
        val response = testService.handleRequest("south")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: N]\n")
    }

    @Test
    fun testCanMoveEast() {
        // setup
        val testService = createTestService()

        // given
        val destinationId: Int
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            testService.startRoom.id,
            Exit(Direction.East, destinationId),
        )

        // when
        val response = testService.handleRequest("east")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: W]\n")
    }

    @Test
    fun testCanMoveWest() {
        // setup
        val testService = createTestService()

        // given
        val destinationId: Int
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            testService.startRoom.id,
            Exit(Direction.West, destinationId),
        )

        // when
        val response = testService.handleRequest("west")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: E]\n")
    }

    @Test
    fun testCanMoveUp() {
        // setup
        val testService = createTestService()

        // given
        val destinationId: Int
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            testService.startRoom.id,
            Exit(Direction.Up, destinationId),
        )

        // when
        val response = testService.handleRequest("up")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: D]\n")
    }

    @Test
    fun testCanMoveDown() {
        // setup
        val testService = createTestService()

        // given
        val destinationId: Int
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            testService.startRoom.id,
            Exit(Direction.Down, destinationId),
        )

        // when
        val response = testService.handleRequest("down")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: U]\n")
    }

    @Test
    fun testCannotMoveWhenDoorIsClosed() {
        // setup
        val test = createTestService()

        // given
        val destinationId: Int
        test.createRoom(
            transaction {
                RoomEntity.new {
                    name = "foo"
                    description = "bar"
                    exits = mutableListOf()
                }
            }.also {
                destinationId = it.id.value
            },
            1,
            Exit(Direction.North, destinationId, "door", ExitStatus.Closed),
        )

        // when
        val response = test.handleRequest("n")

        // then
        assertThat(response.toActionCreator).isEqualTo("the door is closed")
    }
}
