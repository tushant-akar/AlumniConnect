package com.example.collegeportal.fragments.message


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.collegeportal.R
import com.example.collegeportal.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class CollegeMessageFragment : Fragment() {

    private lateinit var pager: ViewPager
    private lateinit var tab: TabLayout
    private lateinit var bar: Toolbar
    private lateinit var searchButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_college_message,
            container,
            false
        )

        pager = view.findViewById(R.id.viewPager)
        tab = view.findViewById(R.id.tabs)
        bar = view.findViewById(R.id.toolbar)
        searchButton = view.findViewById(R.id.search_btn)

        // To make our toolbar show the application
        // we need to give it to the ActionBar
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(bar)
        }

        // Initializing the ViewPagerAdapter
        val adapter = ViewPagerAdapter(childFragmentManager)

        // add fragment to the list
        adapter.addFragment(PeopleFragment(), "People")
        adapter.addFragment(GroupFragment(), "Groups")

        // Adding the Adapter to the ViewPager
        pager.adapter = adapter

        // bind the viewPager with the TabLayout.
        tab.setupWithViewPager(pager)

        searchButton.setOnClickListener{
            navigateToFragment(parentFragmentManager, SearchUserFragment())
        }

        return view
    }

    fun navigateToFragment(fragmentManager: FragmentManager, fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_1, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
