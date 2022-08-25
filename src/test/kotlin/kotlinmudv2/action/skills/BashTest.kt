package kotlinmudv2.action.skills

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.skill.SkillName
import kotlinmudv2.test.createTestService
import kotlin.test.Test

class BashTest {
    @Test
    fun testCanBashSuccessfully() {
        // setup
        val test = createTestService()

        test.getPlayerMob().skills[SkillName.Bash] = 1
        test.createMob()
        test.setupFight()

        val response = test.repeatUntilSuccessful("bash") {
            it.moves = 100
        }

        assertThat(response?.actionStatus).isEqualTo(ActionStatus.Success)
    }

    @Test
    fun testCanFailBashing() {
        // setup
        val test = createTestService()

        test.getPlayerMob().skills[SkillName.Bash] = 1
        test.createMob()
        test.setupFight()

        val response = test.repeatUntilFailed("bash") {
            it.moves = 100
        }

        assertThat(response?.actionStatus).isEqualTo(ActionStatus.Failure)
    }
}
