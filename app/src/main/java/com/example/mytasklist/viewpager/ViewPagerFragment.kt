package com.example.mytasklist.viewpager

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mytasklist.R
import com.example.mytasklist.database.TaskDatabase
import com.example.mytasklist.databinding.DialogAddBinding
import com.example.mytasklist.databinding.FragmentViewpagerBinding
import com.example.mytasklist.tasklist.TaskListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

//タブの数
private const val NUM_TABS = 3
//ViewModelをDialogと共有
private lateinit var viewModel: ViewPagerViewModel

class ViewPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //bindingする
        val binding: FragmentViewpagerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_viewpager, container, false
        )

        //DAOのインスタンスを取得する
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        //ViewModelを取得する
        val viewModelFactory = ViewPagerViewModelFactory(dataSource)
        val listPagerViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ViewPagerViewModel::class.java)
        viewModel = listPagerViewModel

        //ライフサイクルの同期
        binding.lifecycleOwner = viewLifecycleOwner

        //ViewPagerの設定
        viewPager = binding.pager
        val fa = this.activity
        val pagerAdapter = fa?.let { PagerAdapter(it) }
        viewPager.adapter = pagerAdapter

        //ページごとにタブの色を変える
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            //タブ選択時、インジケータ色を変更
            override fun onTabSelected(tab: TabLayout.Tab) {
                context?.let {
                    when (tab.position) {
                        0 -> binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(it, R.color.tab01))
                        1 -> binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(it, R.color.tab02))
                        2 -> binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(it, R.color.tab03))
                    }
                }
                //viewModelのpositionを更新
                viewModel.position.value = tab.position
            }
            //選択解除時、何もしない
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            //再選択時、何もしない
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //タブとページの連動
        TabLayoutMediator(binding.tabLayout, viewPager) { _, _ -> }.attach()

        //fabにリスナーを設定
        binding.addFab.setOnClickListener {
            showDialog()
        }
        return binding.root
    }

    //dialog表示用のメソッド
    private fun showDialog() {
        val dialog = AddDialogFragment()
        dialog.show(parentFragmentManager, "add_dialog")
    }

    //タスク追加用dialog
    class AddDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val binding = DataBindingUtil.inflate<DialogAddBinding>(LayoutInflater.from(activity), R.layout.dialog_add, null, false)
                binding.lifecycleOwner = this
                val view = binding.root
                val position = viewModel.position.value

                builder.setView(view)
                    .setPositiveButton(R.string.save_string
                    ) { _, _ ->
                        //保存処理
                        val taskName = binding.editTaskName.text.toString()
                        if (position != null) {
                            viewModel.save(taskName, position)
                        } else {
                            viewModel.save(taskName, 0)
                        }
                    }
                    .setNegativeButton(R.string.cancel_string
                    ) { _, _ ->
                        dialog?.cancel()
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    //viewPager用のadapterクラス
    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_TABS

        //タブごとに違う内容を表示
        override fun createFragment(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt("POSITION", position)
            val taskListFragment = TaskListFragment()
            taskListFragment.arguments = bundle

            return taskListFragment
        }
    }
}
