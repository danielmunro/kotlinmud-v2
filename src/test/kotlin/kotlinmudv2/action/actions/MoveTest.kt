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
    fun testCanMoveNorth() {
        // setup
        val testService = createTestService()

        // given
        val startRoom = testService.startRoom
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            startRoom.id,
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
        val startRoom = testService.startRoom
        val destinationRoom = testService.createRoom(
            transaction {
                RoomEntity.new {
                    name = "test destination"
                    description = "a description"
                }
            },
            startRoom.id,
            Direction.South,
        )

        // when
        val response = testService.handleRequest("south")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n[Exits: N]\n")
    }
}