package com.gochachko.pavel.tasktracker.logic.task

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.lang.Exception

class Task(uuid : String, title : String, description : String) : Parcelable {
  val uuid : String
  val title : String
  val description : String

  init {
    this.uuid = uuid
    this.title = title
    this.description = description
  }

  constructor(source : Parcel) : this(
    source.readString() ?: "",
    source.readString() ?: "",
    source.readString() ?: ""
  )

  override fun writeToParcel(parcel : Parcel, flags : Int) {
    parcel.writeString(uuid)
    parcel.writeString(title)
    parcel.writeString(description)
  }

  override fun describeContents() : Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Task> {
    override fun createFromParcel(parcel : Parcel) : Task {
      return Task(parcel)
    }

    override fun newArray(size : Int) : Array<Task?> {
      return arrayOfNulls(size)
    }

    fun createTaskByJson(jsonObject : JSONObject) : Task? {
      try {
        val jsonTasks = jsonObject.getJSONArray("objects")
        val jsonTask = jsonTasks.getJSONObject(0)

        return Task(
          jsonTask.getString("uuid"),
          jsonTask.getString("name"),
          jsonTask.getString("description")
        )
      } catch (e : Exception) {
        e.printStackTrace()
      }
      return null
    }
  }
}