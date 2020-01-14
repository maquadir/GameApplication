package com.maq.gameapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.maq.gameapplication.data.Headline

@Dao
interface Headlinedao {

    @Query("SELECT * FROM headline_table")
    fun getAll(): Headline

    @Insert
    fun insertAll(vararg headline:Headline)

    @Insert
    fun insert( headline:Headline)

    @Delete
    fun delete(headline:Headline)
}