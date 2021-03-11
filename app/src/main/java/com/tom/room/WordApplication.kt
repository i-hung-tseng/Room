package com.tom.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

//繼承Application，並在這邊實例化，每次需要的時候都從application呼叫
//by lazy 類似單例模式，var 前面有lateinit，val 使用by lazy
//下面的變數是跟著上面class( WordApplication)的生命週期

//ㄠ

class WordApplication:Application() {

    //不需要去刪除這個Scope，因為它會自己關掉with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    //準備實例化囉，並且因為database是要跟著所有app的生命週期，所以這邊用applicationScope，
    //而不是viewModelScope
    val dataBase by lazy{WordRoomDataBase.getDatabase(this,applicationScope)}
    val repository by lazy{WordRepository(dataBase.wordDao())}

}