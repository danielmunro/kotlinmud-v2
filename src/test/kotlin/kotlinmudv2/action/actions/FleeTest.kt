package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.room.Direction
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class FleeTest {
    @Test
    fun testCanFlee() {
        // setup
        val test = createTestService()
        val destinationRoom = test.createRoom(
            transaction {
                RoomEntity.new {
                    name = "foo"
                    description = "bar"
                }
            },
            1,
            Direction.North,
        )
        test.createMob()

        // given
        test.setupFight()

        // when
        val response = test.handleRequest("flee")

        // then
        assertThat(response.toActionCreator).isEqualTo("you flee running scared!")
        assertThat(test.getPlayerMob().roomId).isEqualTo(destinationRoom.id)
    }

    @Test
    fun testCannotFleeWithNoExitRoom() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        test.setupFight()

        // when
        val response = test.handleRequest("flee")

        // then
        assertThat(response.toActionCreator).isEqualTo("you have nowhere to flee!")
    }

    @Test
    fun testCannotFleeWhenNotFighting() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("flee")

        // then
        assertThat(response.toActionCreator).isEqualTo("you are standing and cannot do that.")
    }
}
