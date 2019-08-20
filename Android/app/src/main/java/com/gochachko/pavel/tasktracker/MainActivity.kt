package com.gochachko.pavel.tasktracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import com.gochachko.pavel.tasktracker.service.ConnectionService
import android.view.Menu
import android.view.MenuItem
import com.gochachko.pavel.tasktracker.fragment.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(),
  NavigationView.OnNavigationItemSelectedListener {

  private lateinit var connectionService : ConnectionService
  private var isBound = false

  override fun onCreate(savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    setSupportActionBar(toolbar)
//
//    val toggle = ActionBarDrawerToggle(
//      this, drawer_layout, toolbar,
//      R.string.navigation_drawer_open,
//      R.string.navigation_drawer_close
//    )
//
//    drawer_layout.addDrawerListener(toggle)
//    toggle.syncState()
//
    nav_view.setNavigationItemSelectedListener(this)
  }

  override fun onStart() {
    super.onStart()
    val intent = Intent(this, ConnectionService::class.java)
    bindService(intent, connectionToService, Context.BIND_AUTO_CREATE)
  }

  override fun onStop() {
    super.onStop()
    if (isBound) {
      unbindService(connectionToService)
      isBound = false
    }
  }

  override fun onCreateOptionsMenu(menu : Menu) : Boolean {
//    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item : MenuItem) : Boolean {
    when (item.itemId) {
      R.id.action_settings -> return true
      else -> return super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(item : MenuItem) : Boolean {

    val transaction = supportFragmentManager.beginTransaction()

    when (item.itemId) {
      R.id.nav_projects -> {
      }
      R.id.nav_tasks -> {
        val taskListFragment = TaskListFragment()
        transaction.replace(R.id.flContainer, taskListFragment)
      }
      R.id.nav_search -> {
      }
      R.id.nav_profile -> {
      }
      R.id.nav_settings -> {
      }
      R.id.nav_view -> {
      }
    }

    transaction.commit()

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }

  fun setToolbar(toolbar : Toolbar) {
    setSupportActionBar(toolbar)

    val toggle = ActionBarDrawerToggle(
      this, drawer_layout, toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )

    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()
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
