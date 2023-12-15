package com.example.studentportal.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentportal.R
import com.example.studentportal.data.Posts

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

    fun bind(post: Posts) {
        profileImageView.setImageResource(R.drawable.ic_post_profile)
        nameTextView.text = post.name
        timestampTextView.text = post.timestamp
        contentTextView.text = post.content
    }
}