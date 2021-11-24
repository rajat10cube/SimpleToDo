package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.onLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)

                //2. Notify the adapter that our data has changed
                adapter.notifyDataSetChanged()

                saveItems()

            }

        }

        val onClickListener = object : TaskItemAdapter.onClickListener{
            override fun onItemClicked(position:Int, oldtask: String) {
                Log.i("Rajat", oldtask.toString())
                val i = Intent(this@MainActivity, EditActivity::class.java)
                i.putExtra("oldtask", oldtask.toString());
                i.putExtra("position", position.toString());
                startActivityForResult(i, 200) // brings up the second activity
            }
        }
        loadItems()
        //Look up recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapterpassing in sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Setup the button and input field, so that user can enter a task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab the text the user has user has inputed
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to out list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //3. Notify the adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //4. Reset text field
            inputTextField.setText("")
            saveItems()
            //Toast.makeText(this@MainActivity, "Task added", Toast.LENGTH_SHORT).show();
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 200) {
            // Extract name value from result extras
            val newtask = data?.getExtras()?.getString("newtask")
            val position = data?.getExtras()?.getString("position")
            listOfTasks.removeAt(position!!.toInt())
            listOfTasks.add(position!!.toInt(), newtask.toString())
            adapter.notifyDataSetChanged()

            saveItems()
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
        }
    }
    //Save the data user has inputed by writing and readign from a file
    //Create a method to get the file we need
    fun getDataFile(): File {
        //Every line is going to represent a task
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line from the file
    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}