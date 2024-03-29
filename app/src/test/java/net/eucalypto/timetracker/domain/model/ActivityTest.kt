package net.eucalypto.timetracker.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

internal class ActivityTest {

    @Nested
    inner class duration() {
        @Test
        fun `returns ZERO for unfinished activity`() {
            val unfinishedActivity = Activity(
                Category("ignore"),
                ZonedDateTime.now(),
                NOT_SET_YET
            )

            val duration = unfinishedActivity.duration

            assertThat(duration).isEqualTo(Duration.ZERO)
        }

        @Test
        fun `returns 1 hour for end time one hour after start time`() {
            val now = ZonedDateTime.now()
            val oneHourAgo = now.minusHours(1)
            val activity = Activity(Category("ignore"), oneHourAgo, now)

            val duration = activity.duration

            assertThat(duration).isEquivalentAccordingToCompareTo(Duration.ofHours(1))
        }

        @Test
        fun `returns 42 seconds end time 42 seconds after start time`() {
            val now = ZonedDateTime.now()
            val fourtyTwoSecondsAgo = now.minusSeconds(42)
            val activity = Activity(Category("ignore"), fourtyTwoSecondsAgo, now)

            val duration = activity.duration

            assertThat(duration).isEquivalentAccordingToCompareTo(Duration.ofSeconds(42))
        }
    }

    @Test
    fun `endTime set - isFinished returns true`() {
        val finishedActivity = Activity(
            Category("dummy category"),
            ZonedDateTime.now(),
            ZonedDateTime.now()
        )

        val isFinished = finishedActivity.isFinished

        assertThat(isFinished).isTrue()
    }

    @Test
    fun `endTime not set - isFinished returns false`() {
        val unfinishedActivity = Activity(
            Category("dummy category"),
            ZonedDateTime.now(),
            NOT_SET_YET
        )

        val isFinished = unfinishedActivity.isFinished

        assertThat(isFinished).isFalse()
    }


    @Test
    fun `withEndTime throws Exception for endTime one Second before startTime`() {
        val now = ZonedDateTime.now()
        val validActivity = Activity(Category("ignore"), now)

        assertThrows<Activity.StartTimeBeforeEndTimeException> {
            validActivity.withEndTime(now.minusSeconds(1))
        }
    }

    @Nested
    inner class withStartTime() {

        @Test
        fun `returns Activity with 1 second earlier startTime when given that new startTime`() {
            val originalStartTime = ZonedDateTime.of(1969, 7, 20, 11, 21, 0, 0, ZoneId.of("Z"))
            val activity = Activity(Category("ignore"), originalStartTime)

            val updatedActivity = activity.withStartTime(originalStartTime.minusSeconds(1))

            assertThat(updatedActivity.startTime)
                .isEquivalentAccordingToCompareTo(originalStartTime.minusSeconds(1))
        }
    }

    @Nested
    inner class constructor() {

        @Test
        fun `throws ActivityFutureTimeException for start or end times being set in the future`() {
            val now = ZonedDateTime.now()
            val inOneHour = now.plusHours(1)

            assertThrows<Activity.FutureTimeException> {
                Activity(Category("ignore"), now, inOneHour)
            }

            assertThrows<Activity.FutureTimeException> {
                Activity(Category("ignore"), inOneHour)
            }
        }

        @Test
        fun `throws ActivityTimeLineException for end Time one Second before startTime`() {
            val now = ZonedDateTime.now()

            assertThrows<Activity.StartTimeBeforeEndTimeException> {
                Activity(Category("ignore"), now, now.minusSeconds(1))
            }
        }
    }
}