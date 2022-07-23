package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class LookAtMobInRoomTest {
    @Test
    fun testCanSeeMobInRoom() {
        val test = createTestService()

        val mob = test.createMob()

        val response = test.handleRequest("look ${mob.brief.split(" ")[0]}")

        assertThat(response.toActionCreator).isEqualTo(mob.brief)
    }
}