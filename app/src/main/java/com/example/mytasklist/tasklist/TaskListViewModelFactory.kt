package com.example.mytasklist.tasklist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytasklist.database.TaskDatabaseDao
import java.lang.IllegalArgumentException

class TaskListViewModelFactory(
    private val database: TaskDatabaseDao,
    private val application: Application,
    private val position: Int) :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            return TaskListViewModel(database, application, position) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}