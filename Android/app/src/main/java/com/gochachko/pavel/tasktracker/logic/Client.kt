package com.gochachko.pavel.tasktracker.logic

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.*
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket

class Client {
  val IP_ADDRESS = "95.165.147.42"
  val PORT = 6789
  val BUFFER_SIZE = 8092

  val CD = CompositeDisposable()

  var dataSubject = PublishSubject.create<ByteArray>()

  var clientSocket : Socket? = null
  var inputStream : DataInputStream? = null
  var outputStream : DataOutputStream? = null


  fun obs() : Observable<ByteArray> {
    return dataSubject.toFlowable(BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread()).toObservable()
  }

  fun connect() {
    CD.add(Observable.create<ByteArray> { emit ->
      try {
        clientSocket = Socket(InetAddress.getByName(IP_ADDRESS), PORT)

        inputStream = DataInputStream(clientSocket?.getInputStream())
        outputStream = DataOutputStream(clientSocket?.getOutputStream())
      } catch (e : Exception) {
        return@create
      }

      inputStream?.let { input ->
        while (true) {
          val byteArray = ByteArray(BUFFER_SIZE)
          val length = input.read(byteArray)

          if (length > 0) {
            emit.onNext(byteArray)
          }

          if (length == -1) {
            break
          }
        }
      }

      emit.onComplete()
    }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { byteArray ->
        dataSubject.onNext(byteArray)
      }
    )
  }

  fun sendBytes(bytesArray : ByteArray, callbackReady : ((ready : Boolean) -> Unit)? = null) {
    CD.add(Observable.create<Boolean> { emit ->
      outputStream?.let { output ->
        output.write(bytesArray)
        output.flush()

        emit.onNext(true)
        emit.onComplete()
        return@create
      }
      emit.onNext(false)
    }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        callbackReady?.invoke(it)
      }
    )
  }

  fun disconnect() {
    try {
      clientSocket?.close()
      inputStream?.close()
      outputStream?.close()
    } catch (e : Exception) {
      e.printStackTrace()
      return
    }

    dataSubject.onComplete()
    CD.clear()
  }
}