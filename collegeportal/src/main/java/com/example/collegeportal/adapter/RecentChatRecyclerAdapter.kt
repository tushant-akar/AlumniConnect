package com.example.collegeportal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.data.Chatroom
import com.example.collegeportal.data.Users
import com.example.collegeportal.fragments.message.ChatFragment
import com.example.collegeportal.util.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecentChatRecyclerAdapter(options: FirebaseRecyclerOptions<Chatroom>, private val context: Context) :
    FirebaseRecyclerAdapter<Chatroom, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options) {

    override fun onBindViewHolder(holder: ChatroomModelViewHolder, position: Int, model: Chatroom) {
        val userEmails = model.usersEmails
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(userEmails?.get(0) ?: "")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val otherUserModel = dataSnapshot.getValue(Users::class.java)
                val lastMessageSentByMe = model.lastMessageSenderEmail == FirebaseUtil.currentUserEmail()

                // Load profile picture using FirebaseUtil.getOtherProfilePicStorageRef(...)
                // Replace this with your logic to load or set a default profile picture
                if (otherUserModel != null) {
                    // Example: Load default profile picture from resources
                    holder.profilePic.setImageResource(R.drawable.ic_profile)
                }

                holder.usernameText.text = otherUserModel?.name ?: ""
                holder.lastMessageText.text =
                    if (lastMessageSentByMe) "You : ${model.lastMessage}" else model.lastMessage
                holder.lastMessageTime.text = FirebaseUtil.timestampToString(model.lastMessageTimestamp)

                holder.itemView.setOnClickListener {
                    // Create an instance of the fragment
                    val fragment = ChatFragment(selectedUser = otherUserModel!!)

                    // Navigate to the ChatFragment
                    val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.people_recycler_row, parent, false)
        return ChatroomModelViewHolder(view)
    }

    inner class ChatroomModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.username_text)
        val lastMessageText: TextView = itemView.findViewById(R.id.last_message_text)
        val lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time_text)
        val profilePic: ImageView = itemView.findViewById(R.id.profile_pic_image_view)
    }
}
