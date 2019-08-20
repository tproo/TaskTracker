package com.gochachko.pavel.tasktracker

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.gochachko.pavel.tasktracker.logic.task.Task
import com.gochachko.pavel.tasktracker.logic.task.TaskManager
import com.gochachko.pavel.tasktracker.service.ConnectionService

class App : Application() {
  companion object {
    lateinit var instance : App
      private set

    lateinit var taskManager : TaskManager
      private set

    lateinit var connectionService : ConnectionService
    var isBound = false
  }

  override fun onCreate() {
    super.onCreate()

    instance = this

    startConnectionService()
    bindToConnectionService()

    taskManager = TaskManager()
  }

  private fun startConnectionService() {
    val indentService = Intent(this, ConnectionService::class.java)
    startService(indentService)
  }


  private fun bindToConnectionService() {
    val intent = Intent(this, ConnectionService::class.java)
    bindService(intent, connectionToService, Context.BIND_AUTO_CREATE)
  }

  private fun stopService() {
    if (isBound) {
      unbindService(connectionToService)
      isBound = false
    }
  }

  val connectionToService = object : ServiceConnection {
    override fun onServiceConnected(name : ComponentName?, service : IBinder?) {
      val binder = service as ConnectionService.ConnectionBinder
      connectionService = binder.getService()
      isBound = true
    }

    override fun onServiceDisconnected(name : ComponentName?) {
      isBound = false
    }
  }
}