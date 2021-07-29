package net.eucalypto.timetracker.export

import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Activity

class CsvExporter(private val repository: Repository) {

    suspend fun exportCsv() {
        val activities = repository.getActivities()
        val csvContent = activityListToCsv(activities)
    }
}

internal fun activityListToCsv(activities: List<Activity>): String {
    val exported = StringBuilder()

    exported.append("CategoryName\n")
    activities.forEach { activity ->
        exported.append(activity.category.name).append("\n")
    }

    return exported.toString()
}