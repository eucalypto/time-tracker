package net.eucalypto.timetracker.activity.list

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

internal class BindingAdaptersKtTest {

    private fun assertFormat(seconds: Long, expected: String) {
        val duration = Duration.ofSeconds(seconds)

        val formatted = duration.toFormattedString()

        assertThat(formatted).matches(expected)
    }

    @Test
    fun toFormattedString() {
        assertFormat(1, "0:00:01")
        assertFormat(60, "0:01:00")
        assertFormat((3600 + 60 + 1).toLong(), "1:01:01")
    }
}