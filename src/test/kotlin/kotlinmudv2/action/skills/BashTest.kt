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

        test.getPlayerMob().skills[SkillName.Bash] = 1
        test.createMob()
        test.setupFight()

        val response = test.repeatUntilSuccessful("bash") {
            it.moves = 100
        }

        assertThat(response?.toActionCreator).isEqualTo("you slam into a test mob and send them flying!")
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

        assertThat(response?.toActionCreator).isEqualTo("you fall flat on your face!")
    }
}
