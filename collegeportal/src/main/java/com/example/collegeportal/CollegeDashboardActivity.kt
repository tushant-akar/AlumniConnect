package com.example.collegeportal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class CollegeDashboardActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_college_dashboard)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view_college)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.collegeHomeFragment -> {
                    loadFragment(CollegeHomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.usersFragment -> {
                    loadFragment(UsersFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collegeMessageFragment -> {
                    loadFragment(CollegeMessageFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collegeFundFragment -> {
                    loadFragment(CollegeFundFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collegeEventFragment -> {
                    loadFragment(CollegeEventsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        loadFragment(CollegeHomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.nav_fragment_college, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}