package net.eucalypto.timetracker.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
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

    @Test
    fun `unfinished activity duration returns ZERO`() {
        val unfinishedActivity = Activity(
            Category("ignore"),
            ZonedDateTime.now(),
            NOT_SET_YET
        )

        val duration = unfinishedActivity.duration

        assertThat(duration).isEqualTo(Duration.ZERO)
    }

    @Test
    fun `end time one hour after start time duration returns 1 hour`() {
        val now = ZonedDateTime.now()
        val inOneHour = now + Duration.ofHours(1)
        val activity = Activity(Category("ignore"), now, inOneHour)

        val duration = activity.duration

        assertThat(duration).isEquivalentAccordingToCompareTo(Duration.ofHours(1))
    }

    @Test
    fun `end time 42 seconds after start time duration returns 42 seconds`() {
        val now = ZonedDateTime.now()
        val in42Seconds = now + Duration.ofSeconds(42)
        val activity = Activity(Category("ignore"), now, in42Seconds)

        val duration = activity.duration

        assertThat(duration).isEquivalentAccordingToCompareTo(Duration.ofSeconds(42))
    }
}