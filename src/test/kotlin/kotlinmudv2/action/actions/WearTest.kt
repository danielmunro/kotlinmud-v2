package kotlinmudv2.action.actions

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class WearTest {
    @Test
    fun testCanWearEquipment() {
        // setup
        val test = createTestService()

        // given
        test.createSwordInInventory()

        // when
        val response = test.handleRequest("wear sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("you wear a sword.")
    }

    @Test
    fun testRemovesEquippedWhenWearingEquipment() {
        // setup
        val test = createTestService()

        // given
        test.createEquippedSword()
        test.createSwordInInventory()

        // when
        val response = test.handleRequest("wear sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("you remove a sword and wear a sword.")
    }

    @Test
    fun testCannotWearItemsThatAreNotEquipment() {
        // setup
        val test = createTestService()

        // given
        test.createPotionInInventory()

        // when
        val response = test.handleRequest("wear potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("that is not equipment.")
    }
}
