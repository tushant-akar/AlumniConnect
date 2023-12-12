package com.example.collegeportal.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.data.ChatMessage
import com.example.collegeportal.util.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRecyclerAdapter(options: FirebaseRecyclerOptions<ChatMessage>, private val context: Context) :
    FirebaseRecyclerAdapter<ChatMessage, ChatRecyclerAdapter.ChatModelViewHolder>(options) {

    override fun onBindViewHolder(holder: ChatModelViewHolder, position: Int, model: ChatMessage) {
        Log.i("haushd", "asjd")
        if (model.senderEmail == FirebaseUtil.currentUserEmail()) {
            holder.leftChatLayout.visibility = View.GONE
            holder.rightChatLayout.visibility = View.VISIBLE
            holder.rightChatTextview.text = model.message
        } else {
            holder.rightChatLayout.visibility = View.GONE
            holder.leftChatLayout.visibility = View.VISIBLE
            holder.leftChatTextview.text = model.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatModelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false)
        return ChatModelViewHolder(view)
    }

    class ChatModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var leftChatLayout: LinearLayout = itemView.findViewById(R.id.left_chat_layout)
        var rightChatLayout: LinearLayout = itemView.findViewById(R.id.right_chat_layout)
        var leftChatTextview: TextView = itemView.findViewById(R.id.left_chat_text_view)
        var rightChatTextview: TextView = itemView.findViewById(R.id.right_chat_text_view)
    }
}
