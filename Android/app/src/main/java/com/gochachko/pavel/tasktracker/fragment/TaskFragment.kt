package com.gochachko.pavel.tasktracker.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gochachko.pavel.tasktracker.R
import com.gochachko.pavel.tasktracker.logic.task.Task
import kotlinx.android.synthetic.main.fragment_task.*

class TaskFragment : Fragment() {
  private val TASK_KEY = "task"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_task, container, false)
    return view
  }

  override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val task = arguments?.getParcelable<Task>(TASK_KEY)
    task?.let {
      tvTitle.text = it.title
      etDescription.setText(it.description)
    }

    tabHost.setup()

    val tabSpec1 = tabHost.newTabSpec("tag1")
    tabSpec1.setContent(R.id.tvTab1)
    tabSpec1.setIndicator("Кот")
    tabHost.addTab(tabSpec1)


    val tabSpec2 = tabHost.newTabSpec("tag2")
    tabSpec2.setContent(R.id.tvTab2)
    tabSpec2.setIndicator("Собака")
    tabHost.addTab(tabSpec2)


    val tabSpec3 = tabHost.newTabSpec("tag3")
    tabSpec3.setContent(R.id.tvTab3)
    tabSpec3.setIndicator("Мышь")
    tabHost.addTab(tabSpec3)

    tabHost.currentTab = 0
  }
}