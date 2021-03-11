package com.tom.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.google.android.material.expandable.ExpandableTransformationWidget

class NewWordActivity : AppCompatActivity() {

    private lateinit var editWordView:EditText

    // TODO: 2021/3/11 這邊用public跟沒有用有麼差別? 
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        editWordView = findViewById(R.id.edit_word)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            var replyIntent = Intent()
            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editWordView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK)
            }
            finish()
        }
    }

    companion object{
        //能用 const val 的地方就不要用 val
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"

    }
}