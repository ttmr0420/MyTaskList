package com.example.mytasklist.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasklist.databinding.ListItemBinding

class TaskViewHolder private constructor(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val taskString: TextView = binding.taskString
    val checkBox: CheckBox = binding.checkBox

    companion object {
        fun from(parent: ViewGroup): TaskViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemBinding.inflate(layoutInflater, parent, false)

            return TaskViewHolder(binding)
        }
    }
}
