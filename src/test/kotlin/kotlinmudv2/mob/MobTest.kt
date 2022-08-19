package kotlinmudv2.mob

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.races.createHumanRace
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class MobTest {
    @Test
    fun testAttributesUseRace() {
        // setup
        val test = createTestService()
        val attributes = createHumanRace().attributes

        // when
        val mob = test.getPlayerMob()

        // then
        assertThat(mob.calc(Attribute.Str)).isEqualTo(attributes.getOrDefault(Attribute.Str, -1))
        assertThat(mob.calc(Attribute.Int)).isEqualTo(attributes.getOrDefault(Attribute.Int, -1))
        assertThat(mob.calc(Attribute.Wis)).isEqualTo(attributes.getOrDefault(Attribute.Wis, -1))
        assertThat(mob.calc(Attribute.Dex)).isEqualTo(attributes.getOrDefault(Attribute.Dex, -1))
        assertThat(mob.calc(Attribute.Con)).isEqualTo(attributes.getOrDefault(Attribute.Con, -1))
        assertThat(mob.calc(Attribute.Hit)).isEqualTo(attributes.getOrDefault(Attribute.Hit, -1))
        assertThat(mob.calc(Attribute.Dam)).isEqualTo(attributes.getOrDefault(Attribute.Dam, -1))
    }

    @Test
    fun testRegenWorksAsExpected() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob().also {
            it.hp = 1
            it.mana = 1
            it.moves = 1
        }

        // when
        mob.regen()

        // then
        assertThat(mob.hp).isGreaterThan(1)
        assertThat(mob.mana).isGreaterThan(1)
        assertThat(mob.moves).isGreaterThan(1)
    }
}
