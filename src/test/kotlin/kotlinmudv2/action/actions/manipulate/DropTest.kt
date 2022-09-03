package kotlinmudv2.action.actions.manipulate

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class DropTest {
    @Test
    fun testCanDropItem() {
        // setup
        val test = createTestService()

        // given
        test.createSwordInInventory()

        // when
        val response = test.handleRequest("drop sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("you drop a sword")
    }

    @Test
    fun testCannotDropItemWithNoDrop() {
        // setup
        val test = createTestService()

        // given
        test.createSwordInInventory().also {
            it.flags.add(ItemFlag.NoDrop)
        }

        // when
        val response = test.handleRequest("drop sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("a sword is bound to you and you cannot drop it")
    }

    @Test
    fun testCannotDropItemThatDoesNotExist() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("drop potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you don't have that")
    }
}
