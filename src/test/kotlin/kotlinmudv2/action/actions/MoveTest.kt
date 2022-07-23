package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        val destinationRoom = transaction {
            RoomEntity.new {
                name = "test destination"
                description = "a description"
                southId = 1
            }
        }
        startRoom.northId = destinationRoom.id.value

        // when
        val response = testService.handleRequest("north")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n")
    }

    @Test
    fun testCanMoveSouth() {
        // setup
        val testService = createTestService()

        // given
        val startRoom = testService.startRoom
        val destinationRoom = transaction {
            RoomEntity.new {
                name = "test destination"
                description = "a description"
                northId = 1
            }
        }
        startRoom.southId = destinationRoom.id.value

        // when
        val response = testService.handleRequest("south")

        // then
        assertThat(response.toActionCreator).isEqualTo("${destinationRoom.name}\n${destinationRoom.description}\n")
    }
}