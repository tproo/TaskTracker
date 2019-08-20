package com.gochachko.pavel.tasktracker.logic.task

import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject
import java.lang.Exception

class TaskManager {
  val tasksSubject = PublishSubject.create<List<Task>>()
  val tasks = mutableMapOf<String, Task>()

  fun addTask(jsonTask : JSONObject) {
    try {
      val task = Task(
        jsonTask.getString("uuid"),
        jsonTask.getString("name"),
        jsonTask.getString("description")
      )
      addTask(task)
    } catch (e : Exception) {
      Log.e("ERROR", "Task not created!")
      e.printStackTrace()
    }
  }

  fun obs() : Observable<List<Task>> {
    return tasksSubject.toFlowable(BackpressureStrategy.LATEST)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .toObservable()
  }

  private fun addTask(task : Task) {
    tasks.put(task.uuid, task)
    tasksSubject.onNext(tasks.values.toList())
  }
}