package net.eucalypto.timetracker.ui.activity.list

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

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

    @Nested
    inner class `asCustomFormatString()` {
        @Test
        fun `displays midnight correctly`() {
            val midnight = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault())

            assertThat(midnight.asCustomFormatString()).isEqualTo("00:00")
        }

        @Test
        fun `displays time in 24h format`() {
            val afternoon = ZonedDateTime.of(2000, 1, 1, 16, 20, 0, 0, ZoneId.systemDefault())

            assertThat(afternoon.asCustomFormatString()).isEqualTo("16:20")
        }
    }


}