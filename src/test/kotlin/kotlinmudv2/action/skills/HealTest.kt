package kotlinmudv2.action.skills

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.skill.SkillName
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class HealTest {
    @Test
    fun testCanHealSelfWhileTargetingOther() {
        // setup
        val test = createTestService()

        // given
        test.getPlayerMob().skills[SkillName.Heal] = 1
        test.createMob()
        test.setupFight()

        // when
        val response = test.repeatUntilSuccessful("cast heal") {
            it.mana = 100
        }

        // then
        assertThat(response?.toTarget).isEqualTo("you feel better!")
    }

    @Test
    fun testCanFailHealing() {
        // setup
        val test = createTestService()

        // given
        test.getPlayerMob().skills[SkillName.Heal] = 1
        val mob = test.getPlayerMob()
        test.createMob()
        test.setupFight()

        // when
        val response = test.repeatUntilFailed("cast heal") {
            it.mana = 100
        }

        // then
        assertThat(response?.toActionCreator).isEqualTo("you lose your concentration")
        assertThat(response?.toTarget).isEqualTo(null)
        assertThat(response?.toRoom).isEqualTo("$mob loses their concentration")
    }
}
