package de.eucalypto.timetracker

import android.app.Application
import timber.log.Timber

class TimeTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}