package net.eucalypto.timetracker.nfc.read

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.eucalypto.timetracker.data.database.entities.asDomainModel
import net.eucalypto.timetracker.data.database.getDatabase
import net.eucalypto.timetracker.domain.model.Category
import timber.log.Timber
import java.util.*

class ReadNfcWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Timber.d("NFC event triggered")

        val categoryId = UUID.fromString(inputData.getString(TEXT_KEY))
        val category = getCategoryById(categoryId)

        withContext(Dispatchers.Main) {
            Toast.makeText(
                applicationContext,
                "You started following activity: ${category?.name ?: "Unknown Activity"}",
                Toast.LENGTH_LONG
            )
                .show()
        }

        return Result.success()
    }

    private suspend fun getCategoryById(categoryId: UUID): Category? {
        val dao = getDatabase(applicationContext).categoryDao
        return dao.byId(categoryId)?.asDomainModel()
    }

    companion object {
        const val WORK_NAME = "net.eucalypto.timetracker.ReadNfcWorker"
        const val TEXT_KEY = "text_key"
    }
}