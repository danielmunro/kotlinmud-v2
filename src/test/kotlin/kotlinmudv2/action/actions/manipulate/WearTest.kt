package kotlinmudv2.action.actions.manipulate

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
        assertThat(response.toActionCreator).isEqualTo("you wear a sword")
    }

    @Test
    fun testCannotWearEquipmentThatIsTooHighLevel() {
        // setup
        val test = createTestService()

        // given
        test.createSwordInInventory {
            it.level = 2
        }

        // when
        val response = test.handleRequest("wear sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("you are not a high enough level to wear that.")
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
        assertThat(response.toActionCreator).isEqualTo("you remove a sword and wear a sword")
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

    @Test
    fun testCannotWearItemsThatDontExist() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("wear sword")

        // then
        assertThat(response.toActionCreator).isEqualTo("you don't have anything like that to wear.")
    }
}
