package com.example.mytasklist.tasklist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasklist.database.Task

class TaskListAdapter(viewModel: TaskListViewModel): ListAdapter<Task, TaskViewHolder>(TaskDiffCallback()) {

    private val taskListViewModel = viewModel

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        //タスク名表示
        holder.taskString.text = item.taskName
        //チェックボックスにリスナーを設定
        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                taskListViewModel.taskFinish(item)
                //isCheckedをfalseに戻さないとチェックがついた状態で追加されてしまう
                holder.checkBox.isChecked = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    //D&D用にhelperを設定する
    val helper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val moveFrom = viewHolder.adapterPosition
                val moveTo = target.adapterPosition
                taskListViewModel.move(moveFrom, moveTo)
                notifyItemMoved(moveFrom, moveTo)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //スワイプ時、特に何もしない
            }
        }
    )

    //diffUtilの判定部分
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

}