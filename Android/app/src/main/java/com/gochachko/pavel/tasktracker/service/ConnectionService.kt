package com.gochachko.pavel.tasktracker.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.gochachko.pavel.tasktracker.logic.Client
import com.gochachko.pavel.tasktracker.logic.destributor.Destributor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject
import java.lang.Exception


class ConnectionService : Service() {

  private val binder = ConnectionBinder()
  private val CD = CompositeDisposable()

  val client = Client()
  val destributor = Destributor()

  override fun onBind(intent : Intent) : IBinder? {
    return binder
  }

  override fun onCreate() {
    super.onCreate()
    client.obs()
      .subscribe { byteArray ->

        val jsonString = String(byteArray)

        try {
          destributor.destribute(JSONObject(jsonString))
        } catch (e : Exception) {
          e.printStackTrace()
        }

        Toast.makeText(applicationContext, jsonString, Toast.LENGTH_SHORT).show()
      }
      ?.let { CD.add(it) }
  }

  override fun onStartCommand(intent : Intent?, flags : Int, startId : Int) : Int {
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

  inner class ConnectionBinder : Binder() {
    fun getService() : ConnectionService {
      return this@ConnectionService
    }
  }

  fun sendBytes(bytes : ByteArray) {
    client.sendBytes(bytes)
  }

  fun obs() : Observable<ByteArray> {
    return client.obs()
  }
}
