package de.eucalypto.timetracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkStatusDatabaseDao {

    @Insert
    fun insert(workStatus: WorkStatus)

    @Update
    fun update(workStatus: WorkStatus)

    @Query("SELECT * FROM work_status_table WHERE workStatusId = :key")
    fun get(key: Long): WorkStatus

    @Query("SELECT * FROM work_status_table ORDER BY workStatusId DESC LIMIT 1")
    fun getLastWorkStatus(): WorkStatus

    @Query("SELECT * FROM work_status_table ORDER BY workStatusId DESC")
    fun getAllWorkStatuses(): List<WorkStatus>

}