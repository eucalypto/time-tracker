package net.eucalypto.timetracker.nfc.read

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.Category
import net.eucalypto.timetracker.domain.model.NOT_SET_YET
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.*


internal enum class Scenario {
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
    private lateinit var timestamp: ZonedDateTime
    private var lastActivity: Activity? = null


    override suspend fun doWork(): Result {
        Timber.d("NFC event triggered")

        timestamp = ZonedDateTime.parse(inputData.getString(TIMESTAMP_STRING_KEY))

        val categoryId = UUID.fromString(inputData.getString(CATEGORY_ID_KEY))
        categoryFromNfc = repository.getCategoryById(categoryId)
            ?: run {
                displayToast(applicationContext.getString(R.string.read_nfc_toast_unknown_activity))
                return Result.failure()
            }
        lastActivity = repository.getLastActivity()

        when (determineScenario(lastActivity, categoryFromNfc)) {
            Scenario.NO_UNFINISHED_ACTIVITY -> {
                createAndInsertNewActivity(categoryFromNfc, startTime = timestamp)
                displayMessageStartActivity()
            }
            Scenario.UNFINISHED_ACTIVITY_SAME_AS_TAG -> {
                finishLastActivity()
                displayMessageFinishActivity()
            }
            Scenario.UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG -> {
                finishLastActivity()
                createAndInsertNewActivity(categoryFromNfc, startTime = timestamp)
                displayMessageFinishLastStartNewActivity()
            }
        }

        return Result.success()
    }

    private suspend fun displayMessageStartActivity() {
        val message =
            applicationContext.getString(
                R.string.read_nfc_toast_start_new_activity,
                categoryFromNfc.name
            )
        displayToast(message)
    }

    private suspend fun displayMessageFinishActivity() {
        val message = applicationContext.getString(
            R.string.read_nfc_toast_finish_last_activity,
            lastActivity!!.category.name
        )
        displayToast(message)
    }

    private suspend fun displayMessageFinishLastStartNewActivity() {
        val message = applicationContext.getString(
            R.string.read_nfc_toast_finish_last_activity_and_start_new_one,
            lastActivity!!.category.name,
            categoryFromNfc.name
        )
        displayToast(message)
    }


    private suspend fun finishLastActivity() {
        repository.update(lastActivity!!.withEndTime(timestamp))
    }

    private suspend fun createAndInsertNewActivity(
        categoryFromNfc: Category,
        startTime: ZonedDateTime
    ) {

        val newActivity = Activity(
            categoryFromNfc,
            startTime,
            NOT_SET_YET
        )
        repository.insert(newActivity)
    }


    private suspend fun displayToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val WORK_NAME = "net.eucalypto.timetracker.ReadNfcWorker"
        const val CATEGORY_ID_KEY = "category_id_key"
        const val TIMESTAMP_STRING_KEY = "timestamp_string_key"
    }
}


internal fun determineScenario(lastActivity: Activity?, categoryFromNfc: Category): Scenario {
    return if (lastActivity == null || lastActivity.isFinished) {
        Scenario.NO_UNFINISHED_ACTIVITY
    } else if (lastActivity.category.id == categoryFromNfc.id) {
        Scenario.UNFINISHED_ACTIVITY_SAME_AS_TAG
    } else {
        Scenario.UNFINISHED_ACTIVITY_DIFFERENT_FROM_TAG
    }
}