package net.eucalypto.timetracker.record

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import net.eucalypto.timetracker.database.WorkStatus
import net.eucalypto.timetracker.database.WorkStatusDatabaseDao
import timber.log.Timber

class AddEventViewModel(
    val database: WorkStatusDatabaseDao
) : ViewModel() {

    private val viewModelJob = Job()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _displayText = MutableLiveData<String>()
    val displayText: LiveData<String>
        get() = _displayText


    fun onIntentReceived(intent: Intent) {

        val ndefMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            ?: return

        val message = ndefMessages.first() as NdefMessage

        val payload = message.records.first().payload

        val status = String(payload)

        _displayText.value = "Starting status: $status"

        // Save status in database

        val workStatus = WorkStatus()
        workStatus.status = status.toInt()
        uiScope.launch {
            insert(workStatus)
            Timber.d("Import WorkStatus in Database: $workStatus")
        }

    }

    private suspend fun insert(workStatus: WorkStatus) {
        withContext(Dispatchers.IO) {
            database.insert(workStatus)
        }
    }
}