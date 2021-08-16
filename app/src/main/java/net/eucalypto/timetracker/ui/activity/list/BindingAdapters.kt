package net.eucalypto.timetracker.ui.activity.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.NOT_SET_YET
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun ZonedDateTime.asCustomFormatString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm") // e.g. 21:43
    return this.format(formatter)
}

@BindingAdapter("activityList")
fun bindActivityList(view: RecyclerView, activityList: List<Activity>?) {
    (view.adapter as ActivityAdapter).submitList(activityList)
}

@BindingAdapter("time")
fun bindTime(view: TextView, dateTime: ZonedDateTime?) {
    val dateNotSetYet = dateTime == NOT_SET_YET
    view.text = if (dateNotSetYet) "" else dateTime?.asCustomFormatString()
}

@BindingAdapter("date")
fun bindDate(view: TextView, dateTime: ZonedDateTime?) {
    view.text = dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE)
}

@BindingAdapter("duration")
fun bindDuration(view: TextView, duration: Duration?) {
    duration?.let {
        view.text = it.toFormattedString()
    }
}

internal fun Duration.toFormattedString(): String {
    val totalSeconds = this.seconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val formatted = "%d:%02d:%02d".format(hours, minutes, seconds)
    return formatted
}