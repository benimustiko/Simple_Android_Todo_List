package com.example.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddTodo: Button
    private lateinit var etTitle: EditText

    private lateinit var rvTodos: RecyclerView
    private var layoutmanager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TodoAdapter.ViewHolder>? = null
    private var listData = ArrayList<TodoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTodos = findViewById(R.id.rvTodos)
        etTitle = findViewById(R.id.etTitle)
        btnAddTodo = findViewById(R.id.btnAddTodo)

        fetchData()

        btnAddTodo.setOnClickListener {
            addData(this)
        }
    }

    @SuppressLint("Range")
    fun fetchData() {
        val adb = DatabaseHelper(this)
        val cursor = adb.queryAll()


        listData.clear()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                Log.d("id", id.toString())
                val title = cursor.getString(cursor.getColumnIndex("title"))
                Log.d("title", title.toString())
                val text = cursor.getString(cursor.getColumnIndex("text"))
                val isDone = cursor.getString(cursor.getColumnIndex("isDone"))
                Log.d("isDone", isDone.toString())

                listData.add(TodoModel(id, title, text, isDone.toInt()))

            } while (cursor.moveToNext())
        }

        layoutmanager = LinearLayoutManager(this@MainActivity)
        rvTodos.layoutManager = layoutmanager
        adapter = TodoAdapter(this@MainActivity, listData)
        rvTodos.adapter = adapter
    }


    private fun addData(context: Context) {
        val adb = DatabaseHelper(context)
        adb.insertData(TodoModel(null, etTitle.text.toString(), null, 0))

        fetchData()
        etTitle.setText("")
    }
}