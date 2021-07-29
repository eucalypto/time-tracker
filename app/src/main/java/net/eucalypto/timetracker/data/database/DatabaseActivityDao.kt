package net.eucalypto.timetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import net.eucalypto.timetracker.data.database.entities.DatabaseActivity

@Dao
interface DatabaseActivityDao {

    @Query(
        """SELECT * FROM activities
            UNION SELECT "", "", "", 0 FROM activity_categories WHERE 0=1
            ORDER BY id DESC"""
        // the second (middle) line:
        // UNION SELECT "", "", "", 0 FROM activity_categories WHERE 0=1
        // is there ONLY to tell Room to consider the table activity_categories as well and toggle
        // an update when that table changes. The always-wrong condition 0=1 makes that we take
        // nothing from that table. When a category name changes in the categories table, we want to
        // update the activity list as well, but since the category name is NOT stored in activities
        // table, it is not updated by default.
    )
    fun getActivitiesAsLiveData(): LiveData<List<DatabaseActivity>>

    @Query("SELECT * FROM activities ORDER BY id DESC")
    suspend fun getActivities(): List<DatabaseActivity>

    @Query("SELECT * FROM activities ORDER BY id DESC LIMIT 1")
    suspend fun getLastActivity(): DatabaseActivity?

    @Update
    suspend fun update(activity: DatabaseActivity)

    @Insert
    suspend fun insert(activity: DatabaseActivity)

    @Query("SELECT * FROM activities WHERE categoryId=(:categoryId)")
    suspend fun getActivitiesWithCategory(categoryId: String): List<DatabaseActivity>

    @Delete
    suspend fun delete(activities: List<DatabaseActivity>)

}