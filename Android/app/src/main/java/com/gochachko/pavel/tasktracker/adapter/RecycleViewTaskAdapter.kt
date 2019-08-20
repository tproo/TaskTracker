package com.gochachko.pavel.tasktracker.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gochachko.pavel.tasktracker.R
import com.gochachko.pavel.tasktracker.logic.task.Task
import kotlinx.android.synthetic.main.item_task.view.*

class RecycleViewTaskAdapter : RecyclerView.Adapter<RecycleViewTaskAdapter.ViewHolder>() {
  private var listener : OnItemClickListener<Task>? = null
  private var taskList = listOf<Task>()

  override fun onCreateViewHolder(viewGroup : ViewGroup, index : Int) : ViewHolder {
    val view = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.item_task, viewGroup, false)

    val holder = ViewHolder(view)
    holder.itemView.setOnClickListener {
      val position = holder.adapterPosition
      if (position != RecyclerView.NO_POSITION) {
        listener?.onItemClicked(position, taskList[position])
      }
    }

    return holder
  }

  override fun onBindViewHolder(viewHolder : ViewHolder, index : Int) {
    viewHolder.bindData(taskList[index])
  }

  override fun getItemCount() : Int {
    return taskList.size
  }

  fun updateData(list : List<Task>) {
    taskList = list
    notifyDataSetChanged()
  }

  fun setOnItemClickListener(listener : OnItemClickListener<Task>) {
    this.listener = listener
  }


  inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
  {
    fun bindData(task : Task) {
      itemView.tvTitle.text = task.title
    }
  }

  interface OnItemClickListener<T> {
    fun onItemClicked(position : Int, item : T)
  }
}