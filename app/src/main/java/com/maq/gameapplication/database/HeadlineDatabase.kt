package com.maq.gameapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maq.gameapplication.data.Headline

@Database(entities = [Headline::class], version = 2, exportSchema = false)
 abstract class HeadlineDatabase:RoomDatabase() {

    abstract fun getHeadlineDao(): Headlinedao

    companion object{

        @Volatile
        private var INSTANCE: HeadlineDatabase? = null


        fun getDatabase(context: Context): HeadlineDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {

                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        HeadlineDatabase::class.java,
                        "headline_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance

                return instance
            }
        }
    }

}