package com.example.mytasklist

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.mytasklist.databinding.DialogSettingThemeBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //toolbarを有効化
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }

    //toolbarを設定
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    //toolbarにMenuItemを設定
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_theme -> {
                //カラーテーマの切り替えダイアログを表示
                showDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //dialog表示用のメソッド
    private fun showDialog() {
        val dialog = ChangeThemeDialogFragment()
        dialog.show(supportFragmentManager, "add_dialog")
    }

    //カラーテーマ変更用のダイアログ
    class ChangeThemeDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val binding = DataBindingUtil.inflate<DialogSettingThemeBinding>(
                    LayoutInflater.from(activity), R.layout.dialog_setting_theme,
                    null, false)
                binding.lifecycleOwner = this
                val view = binding.root

                builder.setView(view)
                    .setPositiveButton(R.string.save_string
                    ) { _, _ ->
                        //カラーテーマの変更処理
                        when (binding.colorThemeButtonGroup.checkedRadioButtonId) {
                            binding.radioButtonLight.id -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            }
                            binding.radioButtonDark.id -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            }
                            binding.radioButtonDefault.id -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            }
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
}