package kotlinmudv2.action.actions.informational

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class InventoryTest {
    @Test
    fun testCanShowItemsInInventory() {
        // setup
        val test = createTestService()

        // given
        test.createPotionInInventory(5)
        test.createSwordInInventory()

        // when
        val response = test.handleRequest("inv")

        // then
        assertThat(response.toActionCreator).isEqualTo("your inventory:\n[  5] a potion\n[  1] a sword")
    }
}
