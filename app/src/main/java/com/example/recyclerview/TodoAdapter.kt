package com.example.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView


class TodoAdapter(private var context: Context, private var todos: MutableList<TodoModel>) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idTodo: TextView = itemView.findViewById(R.id.idTodo)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var isDone: TextView = itemView.findViewById(R.id.isDone)
        var cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
        var btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)
        var btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_todo, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {

            idTodo.text = todos[position].id.toString()
            tvTitle.text = todos[position].title
            isDone.text = todos[position].isDone.toString()
            cbDone.isChecked = todos[position].isDone != 0

            cbDone.setOnClickListener {
                if (cbDone.isChecked) {
                    updateIsDone(idTodo.text.toString(), tvTitle.text.toString(), "", "1")
                    sortData()
                } else {
                    updateIsDone(idTodo.text.toString(), tvTitle.text.toString(), "","0")
                    sortData()
                }
            }

            btnEdit.setOnClickListener {
                editData(idTodo.text.toString(), tvTitle.text.toString(), isDone.text.toString())
            }

            btnDelete.setOnClickListener {
                delete(idTodo.text.toString())
                Toast.makeText(holder.btnDelete.context, "Delete", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount() = todos.size

    private fun updateIsDone(idTodo: String, title: String,text: String, isDone:String) {
        val adb = DatabaseHelper(context)
        adb.updateData(
            TodoModel(
                idTodo.toInt(),
                title,
                text,
                isDone.toInt(),
            )
        )
        (context as MainActivity).fetchData()
    }

    fun sortData() {
        todos.sortBy {
            it.isDone
        }
    }

    private fun editData(id: String, title: String, checkDone: String) {

        val builder = AlertDialog.Builder(context)
        val inflater = (context as MainActivity).layoutInflater
        val view = inflater.inflate(R.layout.form_update, null)

        val etTitle = view.findViewById<EditText>(R.id.etTitleUpdate)
        val idTodo = view.findViewById<TextView>(R.id.idTodoEdit)
        val isDone = view.findViewById<TextView>(R.id.isDoneEdit)
        idTodo.text = id
        etTitle.setText(title)
        isDone.text = checkDone

        builder.setPositiveButton("Ubah") { _, _ ->
            val adb = DatabaseHelper(context)
            adb.updateData(
                TodoModel(
                    (idTodo.text as String).toInt(),
                    etTitle.text.toString(),
                    "",
                    (isDone.text as String).toInt(),
                )
            )
            (context as MainActivity).fetchData()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setView(view)
        val al = builder.create()
        al.show()
    }

    private fun delete(id: String) {
        val adb = DatabaseHelper(context)
        adb.deleteData(id)
        (context as MainActivity).fetchData()
    }
}



