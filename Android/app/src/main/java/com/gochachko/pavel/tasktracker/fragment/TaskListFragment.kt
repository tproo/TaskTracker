package com.gochachko.pavel.tasktracker.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.gochachko.pavel.tasktracker.MainActivity
import com.gochachko.pavel.tasktracker.R
import com.gochachko.pavel.tasktracker.adapter.RecycleViewTaskAdapter
import com.gochachko.pavel.tasktracker.base.BaseFragmentVM
import com.gochachko.pavel.tasktracker.fragment.ViewModel.TaskListViewModel
import com.gochachko.pavel.tasktracker.logic.task.Task
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : BaseFragmentVM<TaskListViewModel>() {
  private val tasksAdapter = RecycleViewTaskAdapter()

  override fun viewModel() = ViewModelProviders.of(this).get(TaskListViewModel::class.java)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_task_list, container, false)
    return view
  }

  override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val activity = activity as MainActivity
    activity.setToolbar(tlbTaskList)

    setHasOptionsMenu(true)
    setup()
    subscribeToVM()
  }

  override fun onCreateOptionsMenu(menu : Menu?, inflater : MenuInflater?) {
    inflater?.inflate(R.menu.task_list, menu)
  }

  override fun onOptionsItemSelected(item : MenuItem) : Boolean {
    when (item.itemId) {
      R.id.action_create -> {
        val taskFragment = TaskFragment()

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContainer, taskFragment)
          .addToBackStack(null)
        transaction.commit()

        return true
      }
      R.id.action_search -> return true
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun setup() {
    tasksAdapter.setOnItemClickListener(object : RecycleViewTaskAdapter.OnItemClickListener<Task> {
      override fun onItemClicked(position : Int, item : Task) {
        val bundle = Bundle()
        bundle.putParcelable("task", item)

        val taskFragment = TaskFragment()
        taskFragment.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContainer, taskFragment)
        .addToBackStack(null)
        transaction.commit()
      }
    })

    rvTasks.layoutManager = LinearLayoutManager(this.requireContext())
    rvTasks.setHasFixedSize(false)
    rvTasks.adapter = tasksAdapter
  }

  private fun subscribeToVM() {
    viewModel?.getTasksObs()?.observe(viewLifecycleOwner, Observer { tasks ->
      tasks?.let {
        tasksAdapter.updateData(it)
      }
    })
  }
}