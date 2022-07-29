package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.room.Direction
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class MoveTest {
    @Test
    fun testCannotMoveIfOutOfMoves() {
        // setup
        val test = createTestService()
        val destinationRoom = test.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            test.startRoom.id,
            Direction.North,
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
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            testService.startRoom.id,
            Direction.North,
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
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            testService.startRoom.id,
            Direction.South,
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
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            testService.startRoom.id,
            Direction.East,
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
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            testService.startRoom.id,
            Direction.West,
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
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            testService.startRoom.id,
            Direction.Up,
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
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            testService.startRoom.id,
            Direction.Down,
        )

        // when
        val response = testService.handleRequest("down")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: U]\n")
    }
}
