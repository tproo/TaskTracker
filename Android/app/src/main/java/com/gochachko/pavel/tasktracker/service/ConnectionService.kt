package com.gochachko.pavel.tasktracker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.gochachko.pavel.tasktracker.logic.Client
import io.reactivex.disposables.CompositeDisposable


class ConnectionService : Service() {
    private val client = Client()
    private val CD = CompositeDisposable()
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        client.obs()
            .subscribe { byteArray ->
                val message = String(byteArray)
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
             ?.let { CD.add(it) }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (flags.and(START_FLAG_RETRY) == 0) {
//            // TODO Если это повторный запуск, выполнить какие-то действия.
//        } else {
//            // TODO Альтернативные действия в фоновом режиме.
//        }

        client.connect()
        return START_STICKY
    }

    override fun onDestroy() {
        client.disconnect()
        CD.clear()
        super.onDestroy()
    }
}
