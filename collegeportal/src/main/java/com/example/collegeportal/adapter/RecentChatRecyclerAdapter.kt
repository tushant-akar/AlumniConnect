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
import com.google.firebase.database.ValueEventListener

class RecentChatRecyclerAdapter(
    options: FirebaseRecyclerOptions<Chatroom>,
    private val context: Context
) : FirebaseRecyclerAdapter<Chatroom, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.people_recycler_row, parent, false)
        return ChatroomModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatroomModelViewHolder, position: Int, model: Chatroom) {
        model.usersIds?.let {
            FirebaseUtil.getOtherUserFromChatroom(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val lastMessageSentByMe = model.lastMessageSenderId == FirebaseUtil.currentUserId()

                        val otherUserModel = snapshot.getValue(Users::class.java)

                        FirebaseUtil.getOtherProfilePicStorageRef(otherUserModel?.userId!!)
                            .downloadUrl
                            .addOnCompleteListener { t ->
                                if (t.isSuccessful) {
                                    val uri = t.result
                                    // Set the downloaded profile picture URI to the ImageView
                                    holder.profilePic.setImageURI(uri)
                                } else {
                                    // Set default profile picture when download fails
                                    holder.profilePic.setImageResource(R.drawable.ic_profile)
                                }
                            }

                        holder.usernameText.text = otherUserModel.name
                        holder.lastMessageText.text =
                            if (lastMessageSentByMe) "You : ${model.lastMessage}" else model.lastMessage
                        holder.lastMessageTime.text = FirebaseUtil.timestampToString(model.lastMessageTimestamp)

                        holder.itemView.setOnClickListener {
                            // Create an instance of the fragment
                            val fragment = ChatFragment(selectedUser = otherUserModel)

                            // Navigate to the ChatFragment
                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    class ChatroomModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameText: TextView = itemView.findViewById(R.id.username_text)
        var lastMessageText: TextView = itemView.findViewById(R.id.last_message_text)
        var lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time_text)
        var profilePic: ImageView = itemView.findViewById(R.id.profile_pic_image_view)
    }
}
