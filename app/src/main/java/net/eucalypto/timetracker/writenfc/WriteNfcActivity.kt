package net.eucalypto.timetracker.writenfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.eucalypto.timetracker.databinding.WriteNfcActivityBinding

class WriteNfcActivity : AppCompatActivity() {

    private lateinit var adapter: NfcAdapter


    val fooIntent = Intent(this, this::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    var pendingIntent = PendingIntent.getActivity(this, 0, fooIntent, 0)
    val ndefIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
        try {
            addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
    }
    val intentFiltersArray = arrayOf(ndefIntentFilter)
    val techListArray = arrayOf(arrayOf<String>(NfcA::class.java.name))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = WriteNfcActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = NfcAdapter.getDefaultAdapter(this)

        val record = NdefRecord.createMime("text/plain", "This is dog".toByteArray())
        val androidApplicationRecord =
            NdefRecord.createApplicationRecord(this.packageName)
        val message = NdefMessage(record, androidApplicationRecord)


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
        val foo = 42
    }

    override fun onResume() {
        super.onResume()
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


















