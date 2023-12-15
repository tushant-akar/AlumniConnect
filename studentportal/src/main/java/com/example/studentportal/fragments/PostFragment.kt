package com.example.studentportal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.studentportal.R

class PostFragment : Fragment() {

    private lateinit var postButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        // Find the postButton in the inflated view
        postButton = view.findViewById(R.id.postButton)

        postButton.setOnClickListener {
            navigateToFragment(StudentHomeFragment())
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        // Begin the fragment transaction
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        // Replace the current fragment with the provided fragment
        transaction.replace(android.R.id.content, fragment)

        // Add the transaction to the back stack, so pressing back goes back to the previous fragment
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }
}
