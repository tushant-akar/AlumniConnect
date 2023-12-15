package com.example.studentportal.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.studentportal.R
import com.example.studentportal.fragments.ConnectionsFragment
import com.example.studentportal.fragments.NotificationsFragment
import com.example.studentportal.fragments.PostFragment
import com.example.studentportal.fragments.StudentHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class StudentDashboardActivity: AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_student_dashboard)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view_student)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.studentHomeFragment -> {
                    loadFragment(StudentHomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.connectionsFragment -> {
                    loadFragment(ConnectionsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.postFragment -> {
                    loadFragment(PostFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        loadFragment(StudentHomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.nav_fragment_student, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}