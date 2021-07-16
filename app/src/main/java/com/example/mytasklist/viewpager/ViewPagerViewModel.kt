package com.example.mytasklist.viewpager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.database.Task
import com.example.mytasklist.database.TaskDatabaseDao
import kotlinx.coroutines.launch

class ViewPagerViewModel(dataSource: TaskDatabaseDao) : ViewModel(){
    //保存先をDialogと共有する
    val position = MutableLiveData<Int>()
    val database = dataSource

    //タスクの保存メソッド
    fun save(name: String, saveTo: Int) {
        viewModelScope.launch {
            val newTask = Task()
            val lastTask = database.getLastTask()
            newTask.taskName = name
            newTask.taskSaveTo = saveTo
            //タスクが存在するなら連番、ひとつめなら0番目
            if (lastTask != null) {
                newTask.taskSortPosition = lastTask.taskSortPosition + 1
            } else {
                newTask.taskSortPosition = 0
            }
            insert(newTask)
        }
    }

    //データベースに保存するメソッド
    private suspend fun insert(task: Task) {
        database.insert(task)
    }
}
