package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class SayTest {
    @Test
    fun testSayResponse() {
        // setup
        val test = createTestService()

        val response = test.handleRequest("say hello world")

        assertThat(response.toActionCreator).isEqualTo("you say, \"hello world\"")
    }
}
