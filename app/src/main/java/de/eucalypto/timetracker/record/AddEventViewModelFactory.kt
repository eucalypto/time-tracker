package de.eucalypto.timetracker.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.eucalypto.timetracker.database.WorkStatusDatabaseDao

class AddEventViewModelFactory(
    private val dataSource: WorkStatusDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEventViewModel::class.java)) {
            return AddEventViewModel(dataSource) as T
        } // else
        throw IllegalArgumentException("Unknown VieModel Class $modelClass")
    }
}