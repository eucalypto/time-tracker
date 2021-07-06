package net.eucalypto.timetracker.nfc.read

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import timber.log.Timber
import java.time.LocalDateTime

class ReadNfcActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (NfcAdapter.ACTION_NDEF_DISCOVERED != intent.action) return

        val ndefMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
        val message = ndefMessages.first() as NdefMessage
        val payload = message.records.first().payload
        val text = String(payload)

        val timestamp = LocalDateTime.now().toString()

        val inputData = Data.Builder()
            .putString(ReadNfcWorker.CATEGORY_ID_KEY, text)
            .putString(ReadNfcWorker.TIMESTAMP_STRING_KEY, timestamp)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(ReadNfcWorker::class.java)
            .setInputData(inputData)
            .build()

        Timber.d("trying to start ReadNfcWorker")
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            ReadNfcWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )


        finish()
    }
}