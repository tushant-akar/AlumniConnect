package com.example.collegeportal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.data.Users
import com.example.collegeportal.fragments.message.ChatFragment
import com.example.collegeportal.util.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class SearchUserRecyclerAdapter(
    options: FirebaseRecyclerOptions<Users>,
    private val context: Context
) : FirebaseRecyclerAdapter<Users, SearchUserRecyclerAdapter.UserModelViewHolder>(options) {

    override fun onBindViewHolder(holder: UserModelViewHolder, position: Int, model: Users) {
        holder.usernameText.text = model.name
        holder.emailText.text = model.email
        if (model.email == FirebaseUtil.currentUserEmail()) {
            holder.usernameText.text = "${model.name} (Me)"
        }

        // Note: Firebase Realtime Database doesn't provide direct download URLs for storage references.
        // You may need to adjust your storage structure accordingly.

        holder.itemView.setOnClickListener {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val chatFragment = ChatFragment(model)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chatFragment)
                .addToBackStack(null)
                .commit()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserModelViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false)
        return UserModelViewHolder(view)
    }

    inner class UserModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.username_text)
        val emailText: TextView = itemView.findViewById(R.id.email_text)
    }
}
