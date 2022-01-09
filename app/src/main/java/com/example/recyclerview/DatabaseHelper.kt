package com.example.recyclerview

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHelper(private val context: Context) {

    companion object {
        private const val DB_NAME = "todos.db"
        private const val TB_NAME = "tb_todos"
    }

    private fun openDatabase(): SQLiteDatabase {
        val dbFile = context.getDatabasePath(DB_NAME)

        // Jika database belum ada di device, maka device akan mengkopi database yang ada di folder assets
        // Jika sudah ada, maka device akan memanfaatkan hasil copy
        if (!dbFile.exists()) {
            try {
                val checkDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null)
                checkDB?.close()
                copyDatabase(dbFile)
            } catch (e: IOException) {
                throw RuntimeException("Error creating source database", e)
            }

        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)

    }

    // Digunakan untuk memanggil file database yang ada di folder assets
    @SuppressLint("WrongConstant")
    private fun copyDatabase(dbFile: File) {
        val `is` = context.assets.open(DB_NAME)
        Log.d("tag", "copyDatabase: $`is`")
        val os = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)
        while (`is`.read(buffer) > 0) {
            os.write(buffer)
            Log.d("#DB", "writing>>")
        }

        os.flush()
        os.close()
        `is`.close()
        Log.d("#DB", "completed..")
    }

    fun queryAll(): Cursor{
        val db = openDatabase()
        return db.rawQuery("SELECT * FROM $TB_NAME", null)
    }

    fun insertData(model: TodoModel){
        val db = openDatabase()
        val values = ContentValues()
//        values.put("id", model.text)
        values.put("title", model.title)
        values.put("text", model.text)
        values.put("isDone", model.isDone)
        db.insert(TB_NAME, null, values)
    }

    fun updateData(model: TodoModel) {
        val db = openDatabase()
        val values = ContentValues()
        values.put("title", model.title)
        values.put("text", model.title)
        values.put("isDone", model.isDone)

        db.update(TB_NAME, values, "id" + " = ? ",
            arrayOf((model.id.toString()))
        )
    }

    fun deleteData(id:String) {
        val db = openDatabase()
        db.delete(TB_NAME, "id =$id", null) > 0
    }
}