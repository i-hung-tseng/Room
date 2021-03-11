package com.tom.room

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


//給予參數，就算activity recreated ，還是拿到正確的WordViewModel
class WordViewModel(private val repository: WordRepository ):ViewModel() {

    //Repository已經因為ViewModel被與UI完全分離

    //所以要用asLiveData()去轉換
    //新增LiveData去快取資料 word，allWords在 Repository已經被轉為Flow了
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    //透過發射新的coroutine去無鎖插入新Data (讓需要資料的時候，讓它自動通知)
    fun insert(word:Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

class WordViewModelFactorty(private val repository: WordRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknoew ViewModel class")
    }
}