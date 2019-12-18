package com.example.praxisprojekt

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

import com.example.praxisprojekt.fragmente.CourseFragment
import com.example.praxisprojekt.fragmente.SettingFragment
import com.example.praxisprojekt.fragmente.UserEditFragment
import com.example.praxisprojekt.fragmente.UserFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        // keys for Shared Preferences
        const val USER_ID = "userID"
        const val USER_NAME = "Benutzername"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val header : View = navView.getHeaderView(0)
        val name : TextView = header.drawerUserName
        val image : ImageView = header.drawerUserProfil
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        name.text = sharedPref.getString(USER_NAME, "Geht Nichts")

        image.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserFragment()).commit()
        }


        if (savedInstanceState != null) return
        else {
             val firstFrag = CourseFragment(1)
            firstFrag.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, firstFrag)
                .commit()
        }

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CourseFragment(2)).commit()
            }
            R.id.nav_note -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UserEditFragment()).commit()
            }
            R.id.nav_course -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CourseFragment(3)).commit()
            }
            R.id.nav_sett -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingFragment()).commit()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun loadFragment(fragment: Fragment?): Boolean {
        fragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commit()
        } ?: return false

        return true
    }

}
