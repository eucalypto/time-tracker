package net.eucalypto.timetracker.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class ActivityTest {

    @Test
    fun `endTime set - isFinished returns true`() {
        val finishedActivity = Activity(
            Category("dummy category"),
            ZonedDateTime.now(),
            ZonedDateTime.now()
        )

        val isFinished = finishedActivity.isFinished()

        assertThat(isFinished).isTrue()
    }

    @Test
    fun `endTime not set - isFinished returns false`() {
        val unfinishedActivity = Activity(
            Category("dummy category"),
            ZonedDateTime.now(),
            NOT_SET_YET
        )

        val isFinished = unfinishedActivity.isFinished()

        assertThat(isFinished).isFalse()
    }
}