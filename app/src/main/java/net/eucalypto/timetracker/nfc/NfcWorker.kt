package net.eucalypto.timetracker.nfc

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class NfcWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Timber.d("Marco!")

        val message = inputData.getString(TEXT_KEY)

        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, "Polo! Message: $message", Toast.LENGTH_LONG)
                .show()
        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "net.eucalypto.timetracker.NfcWorker"
        const val TEXT_KEY = "text_key"
    }
}