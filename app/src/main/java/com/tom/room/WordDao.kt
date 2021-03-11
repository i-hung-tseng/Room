package com.tom.room

import android.media.MediaPlayer
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


//以下很多方法都是用 suspend fun ，可以保證不是在主要的Thread
//Dao一定要是interface 或是 abstract
@Dao
interface WordDao{
    //加載所有的list
    @Query("Select * from word_table order by word asc")
    //一個funtion去得到所有的Word，並且return list of words

    //當Dao return Flow時，它自動異步run 在Background
    fun getAlphabetizedWords(): Flow<List<Word>>

    //如果要過濾可以透過下方方法
//    @Query("SELECT * FROM user WHERE age > :minAge")
//    fun loadAllUsersOlderThan(minAge:Int):Array<User>



    //裡面的(onConflict = OnConflictStrategy.IGNORE) :若insert的資料already in List,than Ignore it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word:Word)

    //因沒適合的 annotation for deleteAll()，所以用@query
    @Query("delete from word_table")
    suspend fun deleteAll()
}