package com.example.simpletodoapp

import android.content.Context
import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    var todos : List<ToDo>,
   // val context : Context
) : RecyclerView.Adapter<Adapter.TodoViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun ooClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        mListener = listener
    }

    inner class TodoViewHolder(itemView : View, listener : onItemClickListener): RecyclerView.ViewHolder(itemView){
       val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
       val cbDone = itemView.findViewById<CheckBox>(R.id.cbDone)

        init {
            itemView.setOnClickListener {
                listener.ooClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_view_design,
            parent,
            false
        )
        return TodoViewHolder(view,mListener)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.tvTitle.text = todos[position].task
        holder.cbDone.isChecked = todos[position].isChecked
    }

}
