package net.eucalypto.timetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import net.eucalypto.timetracker.data.database.entities.DatabaseActivity

@Dao
interface DatabaseActivityDao {

    @Query("SELECT * FROM activities")
    fun getActivitiesAsLiveData(): LiveData<List<DatabaseActivity>>

}