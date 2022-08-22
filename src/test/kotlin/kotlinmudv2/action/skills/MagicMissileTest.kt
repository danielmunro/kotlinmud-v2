package kotlinmudv2.action.skills

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        test.getPlayerMob().skills[SkillName.MagicMissile] = 1
        test.createMob()
        test.setupFight()

        // when
        val response = test.repeatUntilSuccessful("cast magic") {
            it.mana = 100
        }

        // then
        assertThat(response?.toActionCreator).isEqualTo("your magic missile grazes a test mob")
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
        assertThat(response?.toActionCreator).isEqualTo("your magic missile grazes a test mob")
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
        assertThat(response?.toActionCreator).isEqualTo("your magic missile grazes a test mob")
    }
}
