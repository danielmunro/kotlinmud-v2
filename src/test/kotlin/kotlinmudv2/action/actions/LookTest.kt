package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class LookTest {
    @Test
    fun testCanLookAndDescribeRoom() {
        // given
        val testService = createTestService()
        val room = testService.startRoom

        // when
        val response = testService.handleRequest("look")

        // then
        assertThat(response.toActionCreator).isEqualTo("${room.name}\n${room.description}\n[Exits: ]\n")
    }
}