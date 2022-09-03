package kotlinmudv2.action.actions.manipulate

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class RemoveTest {
    @Test
    fun testCanRemoveEquipment() {
        // setup
        val test = createTestService()

        // given
        test.createEquippedSword()

        // when
        val response = test.handleRequest("remove sword")

        assertThat(response.toActionCreator).isEqualTo("you remove a sword.")
    }

    @Test
    fun testCannotRemoveEquipmentThatIsNotEquipped() {
        // setup
        val test = createTestService()

        // when
        val response = test.handleRequest("remove sword")

        assertThat(response.toActionCreator).isEqualTo("you are not wearing that.")
    }
}
