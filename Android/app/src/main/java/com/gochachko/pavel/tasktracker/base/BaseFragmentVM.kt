package com.gochachko.pavel.tasktracker.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragmentVM<T> : BaseFragment() {
  protected var viewModel : T? = null

  protected abstract fun viewModel() : T

  override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = viewModel()
  }
}