package com.example.collegeportal.fragments.message


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.adapter.SearchUserRecyclerAdapter
import com.example.collegeportal.data.Users
import com.example.collegeportal.util.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerOptions


class SearchUserFragment : Fragment() {

    private lateinit var searchButton: ImageButton
    private lateinit var searchInput: EditText
    private lateinit var backButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchUserRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_search_user, container, false)

        searchInput = view.findViewById(R.id.search_username_input)
        searchButton = view.findViewById(R.id.search_btn)
        backButton = view.findViewById(R.id.back_btn)
        recyclerView = view.findViewById(R.id.search_user_recycler_view)

        searchInput.requestFocus()

        setupSearchRecyclerView("Tushant")

        backButton.setOnClickListener{
            navigateToFragment(parentFragmentManager, CollegeMessageFragment())
        }

        searchButton.setOnClickListener {
            val searchTerm = searchInput.text.toString()
            if (searchTerm.isEmpty()) {
                searchInput.error = "Please enter name"
            }
            setupSearchRecyclerView(searchTerm)
        }
        return view
    }

    private fun setupSearchRecyclerView(searchTerm: String) {
        val query = FirebaseUtil.allUserDatabaseReference()
            .orderByChild("name")
            .startAt(searchTerm)
            .endAt(searchTerm + "\uf8ff")

        val options = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(query, Users::class.java)
            .build()

        // Update the adapter reference
        adapter = SearchUserRecyclerAdapter(options, requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    private fun navigateToFragment(fragmentManager: FragmentManager, fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_1, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}