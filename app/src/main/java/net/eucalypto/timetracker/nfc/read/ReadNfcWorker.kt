package net.eucalypto.timetracker.nfc.read

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.database.entities.asDomainModel
import net.eucalypto.timetracker.data.database.getDatabase
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.Category
import net.eucalypto.timetracker.domain.model.NOT_SET_YET
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.*


enum class Scenario {
    NO_UNFINISHED_ACTIVITY,
    UNFINISHED_ACTIVITY_SAME_AS_TAG,
    UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG
}

class ReadNfcWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    private val repository = getRepository(applicationContext)
    private lateinit var categoryFromNfc: Category
    private lateinit var lastActivity: Activity
    private lateinit var timestamp: ZonedDateTime


    override suspend fun doWork(): Result {
        Timber.d("NFC event triggered")

        val categoryId = UUID.fromString(inputData.getString(CATEGORY_ID_KEY))
        categoryFromNfc = repository.getCategoryById(categoryId)
            ?: run {
                displayToast(applicationContext.getString(R.string.read_nfc_toast_unknown_activity))
                return Result.failure()
            }
        timestamp = ZonedDateTime.parse(inputData.getString(TIMESTAMP_STRING_KEY))

        repository.getLastActivity()?.let {
            lastActivity = it
        }

        when (determineScenario()) {
            Scenario.NO_UNFINISHED_ACTIVITY -> {
                val newActivityName = createAndInsertNewActivity()
                val message =
                    applicationContext.getString(
                        R.string.read_nfc_toast_start_new_activity,
                        newActivityName
                    )
                displayToast(message)
            }
            Scenario.UNFINISHED_ACTIVITY_SAME_AS_TAG -> {
                finishLastActivity()
                val message = applicationContext.getString(
                    R.string.read_nfc_toast_finish_last_activity,
                    lastActivity.category.name
                )
                displayToast(message)
            }
            Scenario.UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG -> {
                finishLastActivity()
                val newActivityName = createAndInsertNewActivity()
                val message = applicationContext.getString(
                    R.string.read_nfc_toast_finish_last_activity_and_start_new_one,
                    lastActivity.category.name,
                    newActivityName
                )
                displayToast(message)
            }
        }

        return Result.success()
    }


    private suspend fun finishLastActivity() {
        repository.update(lastActivity.withEndTime(timestamp))
    }

    private suspend fun createAndInsertNewActivity(): String {
        val newActivity = Activity(
            categoryFromNfc,
            timestamp,
            NOT_SET_YET
        )
        repository.insert(newActivity)
        return newActivity.category.name
    }

    private fun determineScenario() =
        if (!::lastActivity.isInitialized || lastActivity.isFinished()) {
            Scenario.NO_UNFINISHED_ACTIVITY
        } else if (lastActivity.category == categoryFromNfc) {
            Scenario.UNFINISHED_ACTIVITY_SAME_AS_TAG
        } else {
            Scenario.UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG
        }

    private suspend fun displayToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun getCategoryById(categoryId: UUID): Category? {
        val categoryDao = getDatabase(applicationContext).categoryDao
        return categoryDao.byId(categoryId)?.asDomainModel()
    }

    companion object {
        const val WORK_NAME = "net.eucalypto.timetracker.ReadNfcWorker"
        const val CATEGORY_ID_KEY = "category_id_key"
        const val TIMESTAMP_STRING_KEY = "timestamp_string_key"
    }
}