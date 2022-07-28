package kotlinmudv2.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.game.Attribute
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class MobTest {
    @Test
    fun testAttributesUseRace() {
        // setup
        val test = createTestService()
        val attributes = raceAttributes[Race.Human]

        // when
        val mob = test.getPlayerMob()

        // then
        assertThat(mob.calc(Attribute.Str)).isEqualTo(attributes?.getOrDefault(Attribute.Str, -1))
        assertThat(mob.calc(Attribute.Int)).isEqualTo(attributes?.getOrDefault(Attribute.Int, -1))
        assertThat(mob.calc(Attribute.Wis)).isEqualTo(attributes?.getOrDefault(Attribute.Wis, -1))
        assertThat(mob.calc(Attribute.Dex)).isEqualTo(attributes?.getOrDefault(Attribute.Dex, -1))
        assertThat(mob.calc(Attribute.Con)).isEqualTo(attributes?.getOrDefault(Attribute.Con, -1))
        assertThat(mob.calc(Attribute.Hit)).isEqualTo(attributes?.getOrDefault(Attribute.Hit, -1))
        assertThat(mob.calc(Attribute.Dam)).isEqualTo(attributes?.getOrDefault(Attribute.Dam, -1))
    }
}
