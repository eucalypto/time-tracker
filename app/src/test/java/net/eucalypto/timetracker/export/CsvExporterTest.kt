package net.eucalypto.timetracker.export

import com.google.common.truth.Truth.assertThat
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.Category
import org.junit.jupiter.api.Test

internal class CsvExporterTest {

    @Test
    fun `activityListToCsv returns correct CSV`() {
        val activities = listOf(Activity(Category("Category 1")))

        val exportedCsv = activityListToCsv(activities)

        val expected = """
            CategoryName
            Category 1
            
        """.trimIndent()

        assertThat(exportedCsv).isEqualTo(expected)
    }
}