package com.gochachko.pavel.tasktracker

import android.app.Application
import android.content.Intent
import com.gochachko.pavel.tasktracker.service.ConnectionService

class App : Application() {
    companion object {
        lateinit var instance : App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        startConnectionService()
    }

    private fun startConnectionService() {
        val indentService = Intent(this, ConnectionService::class.java)
        startService(indentService)
    }

}