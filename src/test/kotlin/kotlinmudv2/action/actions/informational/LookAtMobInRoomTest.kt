package kotlinmudv2.action.actions.informational

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlinmudv2.test.getIdentifyingWord
import kotlin.test.Test

class LookAtMobInRoomTest {
    @Test
    fun testCanSeeMobInRoom() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()

        // when
        val response = test.handleRequest("look ${getIdentifyingWord(mob.brief)}")

        // then
        assertThat(response.toActionCreator).isEqualTo(mob.description)
    }
}
