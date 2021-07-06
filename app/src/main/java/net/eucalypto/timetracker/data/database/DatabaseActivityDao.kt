package net.eucalypto.timetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import net.eucalypto.timetracker.data.database.entities.DatabaseActivity

@Dao
interface DatabaseActivityDao {

    @Query("SELECT * FROM activities")
    fun getActivitiesAsLiveData(): LiveData<List<DatabaseActivity>>

    @Query("SELECT * FROM activities ORDER BY id DESC LIMIT 1")
    suspend fun getLastActivity(): DatabaseActivity?

    @Update
    suspend fun update(activity: DatabaseActivity)

    @Insert
    suspend fun insert(activity: DatabaseActivity)

}