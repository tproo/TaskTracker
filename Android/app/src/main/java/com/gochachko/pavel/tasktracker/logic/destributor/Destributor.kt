package com.gochachko.pavel.tasktracker.logic.destributor

import android.util.Log
import com.gochachko.pavel.tasktracker.App
import com.gochachko.pavel.tasktracker.logic.task.Task
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class Destributor {



  fun destribute(jsonObject : JSONObject) {
    try {
      val objects = jsonObject.getJSONArray("objects")
      for (i in 0 until objects.length()) {
        val obj = objects.getJSONObject(i)
//        val type = convertStringToDataType(obj.getString("type"))
        sendObjectToManager(DataType.TASK, obj)
      }
    } catch (e : Exception) {
      Log.e("ERROR", e.message)
    }
  }

  fun sendObjectToManager(type : DataType, jsonObject : JSONObject) {
    when (type) {
      DataType.TASK -> {
        App.taskManager.addTask(jsonObject)
      }
      DataType.USER -> {

      }
      DataType.PROJECT -> {

      }
      DataType.OTHER -> {

      }
    }
  }
}