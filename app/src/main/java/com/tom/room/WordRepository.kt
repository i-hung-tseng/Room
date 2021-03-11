package com.tom.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


//Repository不是一個特殊的Android元件。它是一個簡單的類，
// 沒有任何特定的實現，它負責從可用資源和資料庫中獲取資料到Web服務。
// 處理所有這些資料，通常將它們轉換為可觀察的LiveData並提供給ViewModel。
//提供

//建構值裡面是interface，透過傳入Dao的方式而不是整個database。因為Repository
// 只要跟Dao溝通就好

//在Repository exposes method 給 viewModel去interact後面的資料來源 (本案例是 Room的 database)

class WordRepository(private val wordaDao:WordDao) {
    val allWords: Flow<List<Word>> = wordaDao.getAlphabetizedWords()

    //Room執行各種 queries 在不同的 thread
    @Suppress("RedundanSuspendModifier")
    @WorkerThread
    suspend fun insert(word:Word){
        wordaDao.insert(word)
    }


}