package net.eucalypto.timetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.eucalypto.timetracker.data.database.entities.DatabaseCategory

@Dao
interface DatabaseCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: DatabaseCategory)

    @Query("SELECT * FROM activity_categories")
    fun categoryList(): LiveData<List<DatabaseCategory>>

}