package com.tom.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    //這邊要了解為什麼要用委派
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactorty((application as WordApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //當點擊fab事件後，跳轉page
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //觀察 ViewModel的 allWords
        //裡面的this代表，這個LiveData會遵照MainActivity的生命週期去判斷是否更新
        // TODO: 2021/3/11 wordViewModel.allWords.observe(owner = this) {words -> words.let{adapter.submitList(it)}
        //
        wordViewModel.allWords.observe(this, Observer { words ->
            words?.let { adapter.submitList(it) }

        })


    }

    // TODO: 2021/3/11 下面的建構值裡面是 intentData
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        Log.d("MainActiviity","進到onActivityResult")

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            Log.d("MainActivity","進到onActivityResult的if")
            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let { reply->
                // TODO: 2021/3/11 原本是要 reply
                val word = Word(reply)
                wordViewModel.insert(word)
                Toast.makeText(this,"$word",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}