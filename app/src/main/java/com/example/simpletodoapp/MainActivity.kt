package com.example.simpletodoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.simpletodoapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    var todoList = mutableListOf<ToDo>()

    lateinit var recycler_view: RecyclerView

    lateinit var fab: FloatingActionButton

    val adapter = Adapter(todoList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        recycler_view = findViewById(R.id.recycle_view)
        fab = findViewById(R.id.floatingActionButton)


        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        //Update task
        adapter.setOnItemClickListener(object : Adapter.onItemClickListener{
            override fun ooClick(position: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.update_dialog,null)
                val update = layout.findViewById<EditText>(R.id.etUpdate)

                with(builder){
                    setTitle("Edit your task")
                    setPositiveButton("Save"){_,_->
                        if (update.text.isNotEmpty()) {
                            Toast.makeText(
                                this@MainActivity,
                                "${update.text} is updated",
                                Toast.LENGTH_SHORT
                            ).show()

                            val updatedTask = update.text.toString()
                            val todo = ToDo(updatedTask, false)
                            todoList.set(position,todo)
                            adapter.notifyItemChanged(position)
                        }

                    }

                    setNegativeButton("Cancel"){_,_->
                        Toast.makeText(
                            this@MainActivity,
                            "Canceled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    setView(layout)
                    show()
                }

            }

        })

        //Add new task function
        showEditTextDialog()

        //Delete task from the list
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourse: ToDo =
                    todoList.get(viewHolder.adapterPosition)

                val position = viewHolder.adapterPosition

                todoList.removeAt(viewHolder.adapterPosition)

                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                Snackbar.make(recycler_view, "Deleted " + deletedCourse.task, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {

                            todoList.add(position, deletedCourse)

                            adapter.notifyItemInserted(position)
                        }).show()


            }

        }).attachToRecyclerView(recycler_view)

    }

    //Insert new task function(implementation)
    fun showEditTextDialog() {
        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_dialog, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.etNewTask)

            with(builder) {
                setTitle("Add New Task")
                setPositiveButton("Add") { dialog, which ->

                    if (editText.text.isNotEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "${editText.text} added to list",
                            Toast.LENGTH_SHORT
                        ).show()

                        val title = editText.text.toString()
                        val todo = ToDo(title, false)
                        todoList.add(todo)
                        adapter.notifyItemInserted(todoList.size - 1)
                    }
                }

                setNegativeButton("Cancel") { dialog, which -> }
                setView(dialogLayout)
                show()
            }
        }
    }

}

