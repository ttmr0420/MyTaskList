package com.example.mytasklist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "task_name")
    var taskName: String = "",

    @ColumnInfo(name = "task_save_to")
    var taskSaveTo: Int = 0,

    @ColumnInfo(name = "task_sort_position")
    var taskSortPosition: Long = 0L,

    @ColumnInfo(name = "task_is_finished")
    var taskFinished: Boolean = false
)