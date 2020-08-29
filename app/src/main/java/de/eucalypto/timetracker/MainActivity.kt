package de.eucalypto.timetracker

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.eucalypto.timetracker.database.WorkStatus
import de.eucalypto.timetracker.database.WorkStatusDatabase
import de.eucalypto.timetracker.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()

//        setupForegroundDispatch(this, nfcAdapter)
    }

    private fun setupForegroundDispatch(mainActivity: MainActivity, nfcAdapter: NfcAdapter?) {
        TODO("Not yet implemented")
    }

    private fun handleIntent(intent: Intent) {

        val extra = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) ?: return

        val message = extra.first() as NdefMessage

        val payload = message.records.first().payload

        val status = String(payload)

        binding.text.text = "Starting status: " + status

        // Save status in database

        val workStatus = WorkStatus()
        workStatus.status = status.toInt()

        val dataSource = WorkStatusDatabase.getInstance(application).workStatusDatabaseDao


        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        uiScope.launch {
            dataSource.insert(workStatus)
        }

        val entries = dataSource.getAllWorkStatuses()

        val printString = StringBuilder()
        for (entry in entries) {
            printString.append(entry)
        }
        binding.text.text = printString.toString()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d("T", "i")
    }
}

private class NdefReaderTask {

}