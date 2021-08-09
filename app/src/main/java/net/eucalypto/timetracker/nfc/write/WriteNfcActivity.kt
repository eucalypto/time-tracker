package net.eucalypto.timetracker.nfc.write

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.databinding.WriteNfcActivityBinding
import net.eucalypto.timetracker.domain.model.util.asCategory
import timber.log.Timber
import java.io.IOException

class WriteNfcActivity : AppCompatActivity() {

    private val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val args: WriteNfcActivityArgs by navArgs()

    private val category by lazy { args.categoryParcel.asCategory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = WriteNfcActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI(binding)
    }

    private fun setUpUI(binding: WriteNfcActivityBinding) {
        binding.activity = this
        binding.category = category
        setTitle(R.string.write_nfc_tag)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        writeMessageToTag(intent)
    }

    private fun writeMessageToTag(intent: Intent) {
        CoroutineScope(Dispatchers.Main).launch {
            val message = createNdefMessage()
            try {
                writeMessage(message, intent)
                Toast
                    .makeText(
                        this@WriteNfcActivity,
                        getString(R.string.toast_message_success, category.name),
                        Toast.LENGTH_LONG
                    )
                    .show()
                this@WriteNfcActivity.finish()
            } catch (e: IOException) {
                Timber.e(e, "Could not write NFC tag for category: ${category.name}")
                Toast.makeText(
                    this@WriteNfcActivity,
                    getString(R.string.toast_message_fail, category.name),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createNdefMessage(): NdefMessage {
        val record = NdefRecord.createMime("text/plain", category.id.toString().toByteArray())
        val androidApplicationRecord =
            NdefRecord.createApplicationRecord(this.packageName)
        return NdefMessage(record, androidApplicationRecord)
    }

    private suspend fun writeMessage(message: NdefMessage, intent: Intent) =
        withContext(Dispatchers.IO) {
            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
            val ndef: Ndef = Ndef.get(tag)

            ndef.use {
                it.connect()
                if (it.isConnected)
                    it.writeNdefMessage(message)
            }
        }

    override fun onResume() {
        super.onResume()
        enableNfcIntentInterception()
    }

    private fun enableNfcIntentInterception() {

        val selfIntent = Intent(this, this::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingSelfIntent = PendingIntent.getActivity(this, 0, selfIntent, 0)

        val ndefIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            addDataType("*/*")
        }
        val intentFiltersArray = arrayOf(ndefIntentFilter)
        val techListArray = arrayOf(arrayOf(NfcA::class.java.name))

        nfcAdapter.enableForegroundDispatch(
            this,
            pendingSelfIntent,
            intentFiltersArray,
            techListArray
        )
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }
}


















