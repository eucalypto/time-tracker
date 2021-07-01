package net.eucalypto.timetracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.eucalypto.timetracker.data.database.entities.DatabaseCategory

@Dao
interface DatabaseCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: DatabaseCategory)
}