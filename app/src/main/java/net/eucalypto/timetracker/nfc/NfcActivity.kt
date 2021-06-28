package net.eucalypto.timetracker.nfc

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import timber.log.Timber

class NfcActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (NfcAdapter.ACTION_NDEF_DISCOVERED != intent.action) return

        val ndefMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
        val message = ndefMessages.first() as NdefMessage
        val payload = message.records.first().payload
        val text = String(payload)

        val inputData = Data.Builder().putString(NfcWorker.TEXT_KEY, text).build()

        val workRequest = OneTimeWorkRequest.Builder(NfcWorker::class.java)
            .setInputData(inputData)
            .build()

        Timber.d("trying to start NfcWorker")
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            NfcWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )


        finish()
    }
}