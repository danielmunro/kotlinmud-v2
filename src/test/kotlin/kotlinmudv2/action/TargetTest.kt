package kotlinmudv2.action

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlinmudv2.skill.skills.cleric.createHealSkill
import kotlinmudv2.skill.skills.warrior.createBashSkill
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class TargetTest {
    @Test
    fun testCanGetANewTarget() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        val skill = createBashSkill()
        val target = test.createMob()

        // when
        val response = doTargeting(mob, skill, target)

        // then
        assertThat(response).isTrue()
        assertThat(mob.target).isEqualTo(target)
    }

    @Test
    fun testWillNotTargetASecondMob() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        val oldTarget = test.createMob()
        mob.target = oldTarget
        val skill = createBashSkill()
        val newTarget = test.createMob()

        // when
        val response = doTargeting(mob, skill, newTarget)

        // then
        assertThat(response).isFalse()
        assertThat(mob.target).isEqualTo(oldTarget)
    }

    @Test
    fun testCanTargetAnotherMobWithNonOffensiveSkills() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        val oldTarget = test.createMob()
        mob.target = oldTarget
        val skill = createHealSkill()
        val newTarget = test.createMob()

        // when
        val response = doTargeting(mob, skill, newTarget)

        // then
        assertThat(response).isTrue()
        assertThat(mob.target).isEqualTo(oldTarget)
    }

    @Test
    fun testNonOffensiveDoesNotSetTarget() {
        // setup
        val test = createTestService()

        // given
        val mob = test.getPlayerMob()
        val target = test.createMob()
        val skill = createHealSkill()

        // when
        val response = doTargeting(mob, skill, target)

        // then
        assertThat(response).isTrue()
        assertThat(mob.target).isNull()
    }
}
