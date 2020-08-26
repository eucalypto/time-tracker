package de.eucalypto.timetracker

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.eucalypto.timetracker.databinding.ActivityMainBinding
import java.nio.charset.StandardCharsets
import kotlin.experimental.and


class MainActivity : AppCompatActivity() {

    companion object {
        const val MIME_TEXT_PLAIN = "text/plain"
    }

    //    private val nfcAdpter: NfcAdapter = NfcAdapter.getDefaultAdapter(applicationContext)
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
        val action = intent.action
        val type = intent.type

        val extra = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) ?: return

        val message = extra.first() as NdefMessage

        val payload = message.records.first().payload
        val textEncoding = if (payload[0] and 128.toByte() == 0.toByte())
            StandardCharsets.UTF_8 else StandardCharsets.UTF_16

        val languageCodeLength = (payload[0] and 63.toByte()).toInt() + 1
        binding.text.text = String(
            payload,
            languageCodeLength,
            payload.size - languageCodeLength,
            textEncoding
        )

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d("T", "i")
    }
}

private class NdefReaderTask {

}