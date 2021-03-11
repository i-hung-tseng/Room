package com.tom.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//為了讓word meaningful to a Room database
@Entity(tableName = "word_table")
class Word(@PrimaryKey@ColumnInfo(name = "word")val word:String)