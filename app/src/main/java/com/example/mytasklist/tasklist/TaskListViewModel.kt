package com.example.mytasklist.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.database.Task
import com.example.mytasklist.database.TaskDatabaseDao
import kotlinx.coroutines.launch

class TaskListViewModel (dataSource: TaskDatabaseDao, application: Application, position: Int) : AndroidViewModel(application) {
    //positionに対応するTaskを取得
    val database = dataSource
    val tasks = database.getTasks(position)

    //完了済みにする
    fun taskFinish(task: Task) {
        viewModelScope.launch {
            //完了フラグを立てて更新
            task.taskFinished = true
            database.update(task)
        }
    }

    //タスクを並び替える
    fun move(moveFrom: Int, moveTo: Int) {
        viewModelScope.launch {
            //移動するTaskを取得する
            val taskMoveFrom = tasks.value?.get(moveFrom)
            //positionを変更、更新
            taskMoveFrom?.taskSortPosition = moveTo.toLong()
            if (taskMoveFrom != null) {
                update(taskMoveFrom)
            }
            //リストに反映
            tasks.value?.add(moveTo, tasks.value!!.removeAt(moveFrom))
            //下で使うカウンターを用意
            var counter = moveTo
            //移動したTask以下のtask_sort_positionを入れ替えていく
            //上方向の移動と下方向の移動で分岐
            when {
                //下から上
                moveTo < moveFrom -> {
                    //taskMoveFromの一つ下のTask(task1)と二つ下のTask(task2)を取得し、
                    //task1のpositionをtask2のpositionで上書きする
                    while ((counter + 1) < moveFrom) {
                        val task1 = tasks.value?.get(counter + 1)
                        val task2 = tasks.value?.get(counter + 2)
                        task1?.taskSortPosition = task2?.taskSortPosition!!
                        if (task1 != null) {
                            //db更新
                            update(task1)
                        }
                        //カウントアップを忘れない
                        counter++
                    }
                    //counter == moveFromになったら最後の一つを更新して終了
                    val lastTask = tasks.value?.get(moveFrom)
                    lastTask?.taskSortPosition = moveFrom.toLong()
                    if (lastTask != null) {
                        update(lastTask)
                    }
                }
                //上から下
                moveTo > moveFrom -> {
                    //taskMoveFromの一つ上のTask(task1)と二つ上のTask(task2)を取得し、
                    //task1のpositionをtask2のpositionで上書きする
                    while ((counter - 1) > moveFrom) {
                        val task1 = tasks.value?.get(counter - 1)
                        val task2 = tasks.value?.get(counter - 2)
                        task1?.taskSortPosition = task2?.taskSortPosition!!
                        if (task1 != null) {
                            //db更新
                            update(task1)
                        }
                        //こっちはカウントダウン
                        counter--
                    }
                    //counter == moveFromになったら最後の一つを更新して終了
                    val lastTask = tasks.value?.get(moveFrom)
                    lastTask?.taskSortPosition = moveFrom.toLong()
                    if (lastTask != null) {
                        update(lastTask)
                    }
                }
            }

        }
    }

    //更新用のメソッド
    private suspend fun update(task: Task) {
        database.update(task)
    }
}