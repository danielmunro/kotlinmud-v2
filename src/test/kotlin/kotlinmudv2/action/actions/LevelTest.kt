package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.mob.MaxLevel
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class LevelTest {
    @Test
    fun testCanLevel() {
        // setup
        val test = createTestService()

        // given
        test.getPlayerMob().also {
            it.debitLevel = true
        }

        // when
        val response = test.handleRequest("level")

        // then
        assertThat(response.toActionCreator).isEqualTo("you gained a level!")
        assertThat(test.getPlayerMob().level).isEqualTo(2)
    }

    @Test
    fun testLevelRequiresADebitLevel() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("level")

        // then
        assertThat(response.toActionCreator).isEqualTo("you have no debit levels available")
    }

    @Test
    fun testLevelCannotExceedMaxLevel() {
        // setup
        val test = createTestService()

        // given
        test.getPlayerMob().also {
            it.debitLevel = true
            it.level = MaxLevel
        }

        // when
        val response = test.handleRequest("level")

        // then
        assertThat(response.toActionCreator).isEqualTo("you are already the maximum level")
    }
}
