package com.example.collegeportal.fragments.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.adapter.RecentChatRecyclerAdapter
import com.example.collegeportal.data.Chatroom
import com.example.collegeportal.util.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class PeopleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecentChatRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_people)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("chatrooms")
        val query: Query = databaseReference.orderByChild("userIds/" + FirebaseUtil.currentUserId()).equalTo(true)

        val options: FirebaseRecyclerOptions<Chatroom> = FirebaseRecyclerOptions.Builder<Chatroom>()
            .setQuery(query, Chatroom::class.java)
            .build()

        adapter = RecentChatRecyclerAdapter(options, requireContext())
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
        adapter.notifyDataSetChanged()
    }
}