package com.example.mytasklist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDatabaseDao {

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * from task_table WHERE taskId = :key")
    fun get(key: Long): Task?

    @Query("SELECT * from task_table ORDER BY taskId DESC LIMIT 1")
    suspend fun getLastTask(): Task?

    @Query("SELECT * from task_table WHERE task_is_finished = 0 AND task_save_to = :position  ORDER BY task_sort_position ASC")
    fun getTasks(position: Int): LiveData<MutableList<Task>>

    @Query("SELECT * from task_table WHERE task_sort_position = :position")
    suspend fun getTaskFromPosition(position: Long): Task
}