package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlinmudv2.test.createTestService
import kotlinmudv2.test.getIdentifyingWord
import kotlin.test.Test

class KillTest {
    @Test
    fun testCanTargetMobInRoom() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()

        // when
        val response = test.handleRequest("kill ${getIdentifyingWord(mob.brief)}")

        // then
        assertThat(response.toActionCreator).isEqualTo("you scream and attack!")
        assertThat(test.getPlayerMob().target).isEqualTo(mob)
    }

    @Test
    fun testCannotTargetMobInDifferentRoom() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        test.moveMob(mob, 0)

        // when
        val response = test.handleRequest("kill ${getIdentifyingWord(mob.brief)}")

        // then
        assertThat(response.toActionCreator).isEqualTo("you don't see them anywhere.")
        assertThat(test.getPlayerMob().target).isNull()
    }
}
