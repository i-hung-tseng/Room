package com.tom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter


//註解這個class是Room Database ，並且夾帶entity
@Database(entities = [Word::class], version = 1)
// TODO: 2021/3/8 為什麼要用abstract class
abstract class WordRoomDataBase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        //單例防止同時給予許多instance
        @Volatile
        private var INSTANCE: WordRoomDataBase? = null


        //為了要launch新的coroutine，所以要CoroutineScope，讓funtion繼承abstract後，
        //才可以拿到CoroutineScope，並起把它放在建構式
        // TODO: 2021/3/10 為什麼需要把虛擬類別 WordRoomDatabase 讓 funtion繼承才可以塞CoroutineScope的建構值
        fun getDatabase(
                context: Context,
                scope: CoroutineScope,
        ): WordRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDataBase::class.java,
                        "word_database"
                )
//                        .fallbackToDestructiveMigration()
                        .addCallback(WordDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }


        //在database 裡面塞一個 RoomDatabase.Callback()，並且建構式裡面塞一個CoroutineScope，
        private class WordDatabaseCallback(
                private val scope: CoroutineScope,
        ) : RoomDatabase.Callback() {

            //因為你不能把Room的database放在UI thread，所以要透過onCreate launches 新的coroutine
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            //在這邊刪除所有 content
            wordDao.deleteAll()

            //新增範例words
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
            word = Word("森怒錢出科特林^^")
            wordDao.insert(word)
        }
    }
}