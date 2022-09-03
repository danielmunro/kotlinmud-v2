package kotlinmudv2.action.actions.manipulate

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class SacrificeTest {
    @Test
    fun testCanSacrificeItemInRoom() {
        // setup
        val test = createTestService()

        // given
        test.createPotionInRoom()
        val worth = test.getPlayerMob().coins

        // when
        val response = test.handleRequest("sac potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("Mojo gives you 1 silver for your sacrifice.")
        assertThat(test.getPlayerMob().coins).isEqualTo(worth + 1)
    }

    @Test
    fun testCanInvokeSacrificeError() {
        // setup
        val test = createTestService()

        // given
        val worth = test.getPlayerMob().coins

        // when
        val response = test.handleRequest("sac potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(test.getPlayerMob().coins).isEqualTo(worth)
    }
}
