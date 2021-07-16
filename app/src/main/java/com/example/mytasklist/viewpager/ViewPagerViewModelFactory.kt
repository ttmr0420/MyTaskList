package com.example.mytasklist.viewpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytasklist.database.TaskDatabaseDao
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewPagerViewModelFactory(private val database: TaskDatabaseDao) :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewPagerViewModel::class.java)) {
            return ViewPagerViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}