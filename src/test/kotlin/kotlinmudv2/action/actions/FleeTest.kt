package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.room.Room
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class FleeTest {
    @Test
    fun testCanFlee() {
        // setup
        val test = createTestService()
        val destinationRoom = Room(
            2,
            "name",
            "description",
            mutableListOf(),
            null,
            1,
            null,
            null,
            null,
            null,
        )
        test.startRoom.northId = 2
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
