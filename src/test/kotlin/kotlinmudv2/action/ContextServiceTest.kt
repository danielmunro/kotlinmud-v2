package kotlinmudv2.action

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlin.test.Test

class ContextServiceTest {
    @Test
    fun testCanParameterizeInput() {
        // when
        val output = ContextService.makeParts("cast 'magic missile' 'bob boberson'")

        // then
        assertThat(output).hasSize(3)
        assertThat(output[0]).isEqualTo("cast")
        assertThat(output[1]).isEqualTo("magic missile")
        assertThat(output[2]).isEqualTo("bob boberson")
    }
}