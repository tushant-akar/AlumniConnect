package com.example.collegeportal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.adapter.UsersAdapter
import com.example.collegeportal.data.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UsersFragment : Fragment() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var tvUsers: TextView
    private lateinit var usersList: ArrayList<Users>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        usersRecyclerView = view.findViewById(R.id.rvUsers)
        usersRecyclerView.layoutManager = LinearLayoutManager(context)
        usersRecyclerView.setHasFixedSize(true)
        tvUsers = view.findViewById(R.id.tvUsers)
        usersList = arrayListOf<Users>()
        getUsersData()
        return view
    }

    private fun getUsersData() {
        usersRecyclerView.visibility = View.GONE
        tvUsers.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Users::class.java)
                        usersList.add(user!!)
                    }
                    val mAdapter = UsersAdapter(usersList)
                    usersRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : UsersAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                                val fragment = UserVerificationFragment().apply {
                                arguments = Bundle().apply {
                                    putString("name", usersList[position].name)
                                    putString("email", usersList[position].email)
                                    putString("mobile", usersList[position].mobileNumber)
                                }
                            }

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    })
                    usersRecyclerView.visibility = View.VISIBLE
                    tvUsers.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}