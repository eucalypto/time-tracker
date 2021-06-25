package net.eucalypto.timetracker.nfc

import android.app.Activity
import android.os.Bundle
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import timber.log.Timber

class NfcActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val workRequest = OneTimeWorkRequest.from(NfcWorker::class.java)
        Timber.d("trying to start NfcWorker")
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            NfcWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )


        finish()
    }
}