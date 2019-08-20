package com.gochachko.pavel.tasktracker.fragment.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.gochachko.pavel.tasktracker.App
import com.gochachko.pavel.tasktracker.base.BaseViewModel
import com.gochachko.pavel.tasktracker.logic.destributor.DataType
import com.gochachko.pavel.tasktracker.logic.query.QueryGenerator
import com.gochachko.pavel.tasktracker.logic.task.Task

class TaskListViewModel : BaseViewModel() {
  private val tasksLiveData = MutableLiveData<List<Task>>().apply { value = mutableListOf() }

  init {
    setup()
  }

  fun getTasksObs() : LiveData<List<Task>> = tasksLiveData

  private fun setup() {
    if (App.isBound) {
      val query = QueryGenerator().forGet(DataType.TASK, null)
      App.connectionService.sendBytes(query.toString().toByteArray())
    }

    App.taskManager.obs()
      .subscribe {
        tasksLiveData.postValue(it)
      }
      ?.let { CD.add(it) }
  }
}