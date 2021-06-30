package net.eucalypto.timetracker.nfc.write

import android.nfc.NdefMessage
import android.nfc.Tag
import android.nfc.tech.Ndef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TagHandler(tag: Tag) {

    private val ndef: Ndef = Ndef.get(tag)

    suspend fun writeMessage(message: NdefMessage) = withContext(Dispatchers.IO) {
        ndef.use { ndef ->
            ndef.connect()
            if (ndef.isConnected)
                ndef.writeNdefMessage(message)
        }
    }
}
