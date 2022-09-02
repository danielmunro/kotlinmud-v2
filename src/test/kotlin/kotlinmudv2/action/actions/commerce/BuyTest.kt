package kotlinmudv2.action.actions.commerce

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class BuyTest {
    @Test
    fun testCanBuyItemFromShopkeeper() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        test.createShopkeeper()
        test.createPotionForShopkeeper()

        // when
        val response = test.handleRequest("buy potion")

        // then
        assertThat(response.toActionCreator).isEqualTo("you buy a potion for 1 gold")
        assertThat(response.toRoom).isEqualTo("$mob buys a potion")
    }
}
