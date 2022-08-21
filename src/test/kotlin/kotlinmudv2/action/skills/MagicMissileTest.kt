package kotlinmudv2.action.skills

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.skill.SkillName
import kotlinmudv2.test.createTestService
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
        val response = test.handleRequest("cast magic")

        // then
        assertThat(response.toActionCreator).isEqualTo("your magic missile grazes a test mob")
    }
}