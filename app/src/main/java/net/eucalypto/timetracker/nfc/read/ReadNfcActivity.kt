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
import java.time.ZonedDateTime

class ReadNfcActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wrongAction = intent.action != NfcAdapter.ACTION_NDEF_DISCOVERED
        if (wrongAction) return

        collectDataAndStartBackgroundWorker()
        finish()
    }

    private fun collectDataAndStartBackgroundWorker() {

        val timestamp = ZonedDateTime.now().toString()
        val categoryId = categoryIdFromIntent()

        val inputData = Data.Builder()
            .putString(ReadNfcWorker.CATEGORY_ID_KEY, categoryId)
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
    }

    private fun categoryIdFromIntent(): String {
        val ndefMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            ?: throw NdefError(
                "intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) returned Null. " +
                        "This should never happen because EXTRA_NDEF_MESSAGES is mandatory for " +
                        "ACTION_NDEF_DISCOVERED and must contain at least one message."
            )
        val message = ndefMessages.first() as NdefMessage
        val payload = message.records.first().payload
        return String(payload)
    }

    class NdefError(message: String) : Exception(message)
}