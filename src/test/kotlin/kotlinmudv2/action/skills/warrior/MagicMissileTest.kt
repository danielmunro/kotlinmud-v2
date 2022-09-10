package kotlinmudv2.action.skills.warrior

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.skill.SkillName
import kotlinmudv2.test.createTestService
import kotlinmudv2.test.getIdentifyingWord
import kotlin.test.Test

class MagicMissileTest {
    @Test
    fun testCanCastMagicMissile() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        mob.skills[SkillName.MagicMissile] = 1
        val target = test.createMob()
        test.setupFight()

        // when
        val response = test.repeatUntilSuccessful("cast magic") {
            it.mana = 100
        }

        // then
        assertThat(response?.toActionCreator).isEqualTo("a bolt of magic leaps from your hand, leaving $target a glancing blow")
        assertThat(response?.toRoom).isEqualTo("a bolt of magic leaps from $mob's hand, leaving $target a glancing blow")
        assertThat(response?.toTarget).isEqualTo("a bolt of magic leaps from $mob's hand, leaving you a glancing blow")
    }

    @Test
    fun testCanTargetAMobWithMagicMissile() {
        // setup
        val test = createTestService()

        // given
        test.getPlayerMob().skills[SkillName.MagicMissile] = 1
        val target = test.createMob()

        // when
        val response = test.repeatUntilSuccessful("cast magic ${getIdentifyingWord(target.name)}") {
            it.mana = 100
        }

        // then
        assertThat(response?.actionStatus).isEqualTo(ActionStatus.Success)
    }

    @Test
    fun testCanTargetAMobWithMagicMissileFullName() {
        // setup
        val test = createTestService()

        // given
        test.getPlayerMob().skills[SkillName.MagicMissile] = 1
        val target = test.createMob()

        // when
        val response = test.repeatUntilSuccessful("cast 'magic missile' ${getIdentifyingWord(target.name)}") {
            it.mana = 100
        }

        // then
        assertThat(response?.actionStatus).isEqualTo(ActionStatus.Success)
    }
}
