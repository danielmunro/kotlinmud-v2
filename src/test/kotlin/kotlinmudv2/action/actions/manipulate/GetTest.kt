package kotlinmudv2.action.actions.manipulate

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class GetTest {
    @Test
    fun testCanGetItemFromRoom() {
        // setup
        val test = createTestService()

        // given
        val item = test.createPotionInRoom()

        // when
        val response = test.handleRequest("get potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you pick up ${item.name} and put it in your inventory.")
        assertThat(test.getPlayerMob().items).hasSize(1)
        assertThat(test.startRoom.items).hasSize(0)
    }

    @Test
    fun testCannotGetItemsThatDoNotExist() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("get potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you don't see that anywhere.")
    }

    @Test
    fun testCannotGetItemsThatAreNotOwnable() {
        // setup
        val test = createTestService()

        // given
        test.createDonationPitInRoom()

        // when
        val response = test.handleRequest("get pit")

        // then
        assertThat(response.toActionCreator).isEqualTo("you cannot pick that up.")
    }
}
