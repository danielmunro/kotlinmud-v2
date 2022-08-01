package kotlinmudv2.fight

import assertk.assertThat
import assertk.assertions.isLessThan
import assertk.assertions.isNull
import kotlinmudv2.game.Attribute
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class FightServiceTest {
    @Test
    fun testFightRoundReducesHpInTarget() {
        // setup
        val test = createTestService()

        // given
        val mob1 = test.getPlayerMob().also {
            it.attributes[Attribute.Hit] = 20
            it.attributes[Attribute.Dam] = 20
        }
        val mob2 = test.createMob().also {
            it.attributes[Attribute.Hit] = 20
            it.attributes[Attribute.Dam] = 20
        }
        test.setupFight()

        // when -- randomness taken into account
        repeat(10) {
            test.executeFight()
        }

        // then
        assertThat(mob1.hp + mob2.hp).isLessThan(mob1.calc(Attribute.Hp) + mob2.calc(Attribute.Hp))
    }

    @Test
    fun testDeathResetsTargets() {
        // setup
        val test = createTestService()
        val mob1 = test.getPlayerMob()
        val mob2 = test.createMob()

        // given
        mob1.attributes[Attribute.Hit] = 20
        mob1.attributes[Attribute.Dam] = 20
        mob2.attributes[Attribute.Hit] = 20
        mob2.attributes[Attribute.Dam] = 20
        test.setupFight()

        // when -- randomness taken into account
        repeat(100) {
            test.executeFight()
        }

        // then
        assertThat(mob1.target).isNull()
        assertThat(mob2.target).isNull()
    }
}
