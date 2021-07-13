package net.eucalypto.timetracker.nfc.read

import com.google.common.truth.Truth.assertThat
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.Category
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class ReadNfcWorkerTest {

    @Nested
    inner class `determineScenario returns` {

        @Test
        fun `NO_UNFINISHED_ACTIVITY when lastActivity is null`() {
            val lastActivity: Activity? = null
            val ignoreCategory = Category("ignore")

            val scenario = determineScenario(lastActivity, ignoreCategory)

            assertThat(scenario).isEqualTo(Scenario.NO_UNFINISHED_ACTIVITY)
        }

        @Test
        fun `NO_UNFINISHED_ACTIVITY when lastActivity is finished`() {
            val category = Category("finding truth")
            val lastActivity = Activity(category, ZonedDateTime.now(), ZonedDateTime.now())

            val scenario = determineScenario(lastActivity, category.withName("ignore"))

            assertThat(scenario).isEqualTo(Scenario.NO_UNFINISHED_ACTIVITY)
        }

        @Test
        fun `UNFINISHED_ACTIVITY_SAME_AS_TAG if category ID of last unfinished activity same as from NFC tag`() {
            val categoryName = "last Category"
            val lastCategory = Category(categoryName)
            val lastActivity = Activity(lastCategory)

            val categoryFromNfc = lastCategory.withName("new name, same old ID")

            val scenario = determineScenario(lastActivity, categoryFromNfc)

            assertThat(scenario).isEqualTo(Scenario.UNFINISHED_ACTIVITY_SAME_AS_TAG)
        }

        @Test
        fun `UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG if category ID of last activity different from NFC tag`() {
            val lastActivity = Activity(Category("last category"))
            val categoryFromNfc = Category("category from NFC")

            val scenario = determineScenario(lastActivity, categoryFromNfc)

            assertThat(scenario).isEqualTo(Scenario.UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG)
        }
    }

}