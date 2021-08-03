package net.eucalypto.timetracker.activity.list.dialog

import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModel
import java.time.ZonedDateTime

class DialogViewModel : ViewModel() {

    lateinit var onDeleteConfirmation: DialogInterface.OnClickListener

    lateinit var timeToDisplay: ZonedDateTime
    var titleId: Int = 0
    lateinit var onTimeSet: TimePickerDialog.OnTimeSetListener
}