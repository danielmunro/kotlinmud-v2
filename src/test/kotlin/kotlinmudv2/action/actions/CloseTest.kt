package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.test.TestService
import kotlinmudv2.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class CloseTest {
    private fun setupDoor(test: TestService, status: ExitStatus = ExitStatus.Open) {
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
            Exit(Direction.North, destinationId, "door", status),
        )
    }

    @Test
    fun testCanCloseDoor() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        setupDoor(test)

        // when
        val response = test.handleRequest("close door")

        // then
        assertThat(response.toActionCreator).isEqualTo("you close the door")
        assertThat(response.toRoom).isEqualTo("$mob closes the door")
    }

    @Test
    fun testCanCloseDoorByDirection() {
        // setup
        val test = createTestService()

        // given
        setupDoor(test)

        // when
        val response = test.handleRequest("close north")

        // then
        assertThat(response.toActionCreator).isEqualTo("you close the door")
    }

    @Test
    fun testCannotCloseLockedDoor() {
        // setup
        val test = createTestService()

        // given
        setupDoor(test, ExitStatus.Locked)

        // when
        val response = test.handleRequest("close north")

        // then
        assertThat(response.toActionCreator).isEqualTo("the door is locked")
    }

    @Test
    fun testCannotCloseDoorThatDoesNotExist() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("close north")

        // then
        assertThat(response.toActionCreator).isEqualTo("you can't close anything like that.")
    }
}
