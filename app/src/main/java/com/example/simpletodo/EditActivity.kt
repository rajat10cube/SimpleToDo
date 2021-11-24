package com.example.simpletodo

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class EditActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_task)
        val editTextField = findViewById<EditText>(R.id.editTextField)
        val oldtask = getIntent().getStringExtra("oldtask")
        val position = getIntent().getStringExtra("position")
        editTextField.setText(oldtask.toString())

        findViewById<Button>(R.id.edit_button).setOnClickListener{
            val newtask = editTextField.text.toString()
            val data = Intent()
            data.putExtra("newtask", newtask)
            data.putExtra("position", position)
            setResult(RESULT_OK, data) // set result code and bundle data for response
            finish() // closes the activity, pass data to parent
        }
    }
}