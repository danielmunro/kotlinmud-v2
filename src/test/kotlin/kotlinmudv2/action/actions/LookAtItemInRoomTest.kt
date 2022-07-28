package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlinmudv2.test.getIdentifyingWord
import kotlin.test.Test

class LookAtItemInRoomTest {
    @Test
    fun testCanLookAtItemsInRoom() {
        // setup
        val test = createTestService()

        // given
        val item = test.createPotionInRoom()

        // when
        val response = test.handleRequest("look ${getIdentifyingWord(item.brief)}")

        // then
        assertThat(response.toActionCreator).isEqualTo(item.description)
    }
}
