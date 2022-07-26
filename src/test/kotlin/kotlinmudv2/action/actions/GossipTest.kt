package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class GossipTest {
    @Test
    fun testGossipResponse() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("gossip hello world")

        // then
        assertThat(response.toActionCreator).isEqualTo("you gossip, \"hello world\"")
    }
}
