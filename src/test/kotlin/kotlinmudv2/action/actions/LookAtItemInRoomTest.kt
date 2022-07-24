package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class LookAtItemInRoomTest {
    @Test
    fun testCanLookAtItemsInRoom() {
        val test = createTestService()
        val item = test.createItemInRoom(test.startRoom.id)
        test.startRoom.items.add(item)

        val response = test.handleRequest("look a")

        assertThat(response.toActionCreator).isEqualTo(item.description)
    }
}