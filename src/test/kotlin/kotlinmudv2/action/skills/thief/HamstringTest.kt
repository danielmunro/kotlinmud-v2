package kotlinmudv2.action.skills.thief

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinmudv2.game.AffectType
import kotlinmudv2.skill.SkillName
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class HamstringTest {
    @Test
    fun testCanHamstringTarget() {
        // setup
        val test = createTestService()
        val mob = test.getPlayerMob()

        // given
        mob.skills[SkillName.Hamstring] = 1
        val target = test.createMob()
        test.setupFight()

        // when
        val response = test.repeatUntilSuccessful("hamstring") {
            it.moves = 100
        }

        // then
        assertThat(response?.toActionCreator).isEqualTo("you hamstring $target, making them limp.")
        assertThat(response?.toTarget).isEqualTo("$mob hamstrings you, making you limp.")
        assertThat(response?.toRoom).isEqualTo("$mob hamstrings $target, making them limp.")
        assertThat(target.affects.find { it.type == AffectType.Hamstrung }).isNotNull()
    }
}
