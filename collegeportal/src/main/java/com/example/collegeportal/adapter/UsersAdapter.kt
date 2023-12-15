package com.example.collegeportal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.data.Users

class UsersAdapter(private val users: ArrayList<Users>):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

        private lateinit var mListener: onItemClickListener

        interface onItemClickListener {
            fun onItemClick(position: Int)
        }

        fun setOnItemClickListener(listener: onItemClickListener) {
            mListener = listener
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.users_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentItem = users[position]
            holder.tvUsersName.text = currentItem.name
        }

        override fun getItemCount(): Int {
            return users.size
        }

    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvUsersName: TextView = itemView.findViewById(R.id.tvUsersName)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}