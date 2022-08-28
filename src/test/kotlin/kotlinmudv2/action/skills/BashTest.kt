package kotlinmudv2.action.skills

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.skill.SkillName
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class BashTest {
    @Test
    fun testCanBashSuccessfully() {
        // setup
        val test = createTestService()

        val mob = test.getPlayerMob()
        mob.skills[SkillName.Bash] = 1
        val target = test.createMob()
        test.setupFight()

        val response = test.repeatUntilSuccessful("bash") {
            it.moves = 100
        }

        assertThat(response?.toActionCreator).isEqualTo("you slam into $target and send them flying!")
        assertThat(response?.toRoom).isEqualTo("$mob slams into $target and sends them flying!")
        assertThat(response?.toTarget).isEqualTo("$mob slams into you and sends you flying!")
    }

    @Test
    fun testCanFailBashing() {
        // setup
        val test = createTestService()

        val mob = test.getPlayerMob()
        mob.skills[SkillName.Bash] = 1
        val target = test.createMob()
        test.setupFight()

        val response = test.repeatUntilFailed("bash") {
            it.moves = 100
        }

        assertThat(response?.toActionCreator).isEqualTo("you fall flat on your face!")
        assertThat(response?.toRoom).isEqualTo("$mob tries to bash $target and falls flat on their face!")
        assertThat(response?.toTarget).isEqualTo("$mob tries to bash you and falls flat on their face!")
    }
}
