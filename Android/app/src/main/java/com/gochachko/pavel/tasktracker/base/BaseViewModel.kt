package com.gochachko.pavel.tasktracker.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
  protected val CD = CompositeDisposable()

  override fun onCleared() {
    CD.clear()
    super.onCleared()
  }
}