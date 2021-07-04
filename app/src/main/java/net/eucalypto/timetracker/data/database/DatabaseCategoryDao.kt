package net.eucalypto.timetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import net.eucalypto.timetracker.data.database.entities.DatabaseCategory
import java.util.*

@Dao
interface DatabaseCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: DatabaseCategory)

    @Query("SELECT * FROM activity_categories")
    fun categoriesAsLiveData(): LiveData<List<DatabaseCategory>>

    @Query("SELECT * FROM activity_categories")
    suspend fun categoryList(): List<DatabaseCategory>


    @Delete
    suspend fun delete(category: DatabaseCategory)

    @Query("SELECT * FROM activity_categories WHERE id=(:id)")
    suspend fun byId(id: UUID): DatabaseCategory?

}