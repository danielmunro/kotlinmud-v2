package kotlinmudv2.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import org.junit.jupiter.api.Test

class ProcessClientBufferObserverTest {
    @Test
    fun testCanLookAndDescribeRoom() {
        // given
        val testService = createTestService()
        val room = testService.startRoom

        // when
        val response = testService.handleRequest("look")

        // then
        assertThat(response.toActionCreator).isEqualTo("${room.name}\n${room.description}\n")
    }
}