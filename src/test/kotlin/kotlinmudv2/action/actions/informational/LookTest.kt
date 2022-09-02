package kotlinmudv2.action.actions.informational

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType
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

    @Test
    fun testLookShowsItemsInRoom() {
        // given
        val testService = createTestService()
        val item = Item(
            0,
            "a potion",
            "hello",
            "a potion is here",
            ItemType.Potion,
            "potion",
            1,
            0,
            mutableListOf(),
            mutableMapOf(),
            mutableMapOf(),
        )

        testService.startRoom.items.add(
            item
        )

        // when
        val response = testService.handleRequest("look")

        // then
        assertThat(item.brief).isNotEqualTo("")
        assertThat(response.toActionCreator).contains(item.brief)
    }

    @Test
    fun testLookShowsMobsInRoom() {
        // given
        val testService = createTestService()
        val mob = testService.createMob()

        // when
        val response = testService.handleRequest("look")

        // then
        assertThat(mob.brief).isNotEqualTo("")
        assertThat(response.toActionCreator).contains(mob.brief)
    }
}
