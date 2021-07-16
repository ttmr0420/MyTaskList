package com.example.mytasklist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 3, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    //databaseとDAOの設定
    abstract val taskDatabaseDao: TaskDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            synchronized(this) {
                var instance = INSTANCE

                //instanceがnullならばinstance作成
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, TaskDatabase::class.java, "task_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}