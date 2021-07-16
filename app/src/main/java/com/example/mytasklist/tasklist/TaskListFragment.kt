package com.example.mytasklist.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytasklist.R
import com.example.mytasklist.database.TaskDatabase
import com.example.mytasklist.databinding.FragmentListBinding

class TaskListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //bindingする
        val binding: FragmentListBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_list, container, false
        )

        //positionを設定
        val bundle = this.arguments
        val position = when {
            bundle != null -> bundle.getInt("POSITION")
            else -> 0
        }

        //applicationを取得
        val application = requireNotNull(this.activity).application
        //DAOのInstanceを取得
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        //ViewModelを取得
        val viewModelFactory = TaskListViewModelFactory(dataSource, application, position)
        val listViewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskListViewModel::class.java)

        //adapterを設定する
        val adapter = TaskListAdapter(listViewModel)
        binding.taskList.adapter = adapter
        adapter.helper.attachToRecyclerView(binding.taskList)

        //リストのLiveDataのObserverを設定する
        listViewModel.tasks.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        //ライフサイクルの同期
        binding.lifecycleOwner = this
        binding.listViewModel = listViewModel

        //LayoutManagerを設定
        val manager = LinearLayoutManager(activity)
        binding.taskList.layoutManager = manager

        //bindingされたlayoutを戻す
        return binding.root
    }
}
