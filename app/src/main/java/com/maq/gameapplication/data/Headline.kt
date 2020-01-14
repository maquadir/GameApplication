package com.maq.gameapplication.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

@Entity(tableName = "headline_table")
@TypeConverters(ItemTypeConverter::class)
data class Headline(

    @ColumnInfo(name = "items")
    val items: List<Item>,

    @ColumnInfo(name = "product")
    val product: String,

    @ColumnInfo(name = "resultSize")
    val resultSize: Int,

    @ColumnInfo(name = "version")
    val version: Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

data class Item(

    @ColumnInfo(name = "correctAnswerIndex")
    val correctAnswerIndex: Int,

    @ColumnInfo(name = "headlines")
    val headlines: List<String>,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "section")
    val section: String,

    @ColumnInfo(name = "standFirst")
    val standFirst: String,

    @ColumnInfo(name = "storyUrl")
    val storyUrl: String
)


class ItemTypeConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Item> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Item>>() {

        }.type

        return gson.fromJson<List<Item>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Item>): String {
        return gson.toJson(someObjects)
    }
}


