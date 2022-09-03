package kotlinmudv2.action.actions.manipulate

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class QuaffTest {
    @Test
    fun testCanQuaffPotion() {
        // setup
        val test = createTestService()

        // given
        val item = test.createPotionInInventory()

        // when
        val response = test.handleRequest("quaff potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you quaff ${item.name}")
        assertThat(response.mob.items).doesNotContain(item)
    }

    @Test
    fun testCannotQuaffEquipment() {
        // setup
        val test = createTestService()

        // given
        val item = test.createSwordInInventory()

        // when
        val response = test.handleRequest("quaff sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("that's not a potion")
        assertThat(response.mob.items).contains(item)
    }

    @Test
    fun testCannotQuaffItemsThatDontExist() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("quaff potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you don't have anything like that to quaff")
    }
}
