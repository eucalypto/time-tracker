package net.eucalypto.timetracker.nfc.write

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.databinding.WriteNfcActivityBinding
import timber.log.Timber
import java.io.IOException

class WriteNfcActivity : AppCompatActivity() {

    private lateinit var adapter: NfcAdapter

    private val args: WriteNfcActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = WriteNfcActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NfcAdapter.getDefaultAdapter(this)

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)


        val message = createNdefMessage()


        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
        val tagHandler = TagHandler(tagFromIntent)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                tagHandler.writeMessage(message)
                Toast
                    .makeText(this@WriteNfcActivity, "Tag written successfully", Toast.LENGTH_LONG)
                    .show()
            } catch (e: IOException) {
                Timber.e(e, "Could not write NFC tag")
                Toast.makeText(this@WriteNfcActivity, "Error writing tag", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun createNdefMessage(): NdefMessage {
        val record = NdefRecord.createMime("text/plain", args.nfcMessage.toByteArray())
        val androidApplicationRecord =
            NdefRecord.createApplicationRecord(this.packageName)
        return NdefMessage(record, androidApplicationRecord)
    }

    override fun onResume() {
        super.onResume()
        enableNfcIntentInterception()
    }

    private fun enableNfcIntentInterception() {
        val fooIntent = Intent(this, this::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, fooIntent, 0)

        val ndefIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            addDataType("*/*")
        }
        val intentFiltersArray = arrayOf(ndefIntentFilter)
        val techListArray = arrayOf(arrayOf(NfcA::class.java.name))

        adapter.enableForegroundDispatch(
            this,
            pendingIntent,
            intentFiltersArray,
            techListArray
        )
    }

    override fun onPause() {
        super.onPause()
        adapter.disableForegroundDispatch(this)
    }
}


















