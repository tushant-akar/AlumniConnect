package com.example.collegeportal.fragments.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeportal.R
import com.example.collegeportal.adapter.ChatRecyclerAdapter
import com.example.collegeportal.adapter.SearchUserRecyclerAdapter
import com.example.collegeportal.data.ChatMessage
import com.example.collegeportal.data.Chatroom
import com.example.collegeportal.data.Users
import com.example.collegeportal.util.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.sql.Timestamp

class ChatFragment(private val selectedUser: Users = Users()) : Fragment() {

    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var tvUserName: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatroomId: String
    private lateinit var chatroomModel: Chatroom
    private lateinit var chatroomReference: DatabaseReference
    private lateinit var adapter: ChatRecyclerAdapter

    // Required empty public constructor
    constructor() : this(Users())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        chatroomId = FirebaseUtil.getChatroomId(
            FirebaseUtil.currentUserEmail()!!,
            selectedUser.email!!
        )
        messageInput = view.findViewById(R.id.msg_input)
        sendButton = view.findViewById(R.id.msg_send_btn)
        backButton = view.findViewById(R.id.back_btn)
        tvUserName = view.findViewById(R.id.tvUsername)
        recyclerView = view.findViewById(R.id.chat_recycler_view)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isEmpty()) {
                messageInput.error = "Please enter message"
            }
            sendMessageToUser(message)
            setupChatRecyclerView()
        }

        tvUserName.text = selectedUser.name
        chatroomReference = FirebaseUtil.getChatroomReference(chatroomId)
        getOrCreateChatroom()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Your code for initializing UI components or performing actions after the view is created
    }

    private fun setupChatRecyclerView() {
        val databaseReference = FirebaseDatabase.getInstance().getReference().child("chatMessages").child(chatroomId)
        val query = databaseReference.orderByChild("timestamp")

        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(query, ChatMessage::class.java)
            .build()

        adapter = ChatRecyclerAdapter(options, requireContext())
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        adapter.startListening()

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                recyclerView.smoothScrollToPosition(0)
            }
        })
    }


    fun sendMessageToUser(message: String) {
        chatroomModel.lastMessageSenderEmail = FirebaseUtil.currentUserEmail()
        chatroomReference.setValue(chatroomModel)

        // Create a ChatMessageModel
        val chatMessageModel =
            ChatMessage(message, FirebaseUtil.currentUserEmail(), System.currentTimeMillis())

        // Add the message to the chatroom's message list
        FirebaseUtil.getChatroomMessageReference(chatroomId).push().setValue(chatMessageModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    messageInput.setText("")
                }
            }
    }

    private fun getOrCreateChatroom() {
        chatroomReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Chatroom exists, retrieve its data
                    chatroomReference.removeEventListener(this)
                    val chatroomData = dataSnapshot.getValue(Chatroom::class.java)
                    chatroomModel = chatroomData ?: Chatroom(
                        chatroomId,
                        listOf(FirebaseUtil.currentUserEmail()!!, selectedUser.email!!),
                        null
                    )
                } else {
                    // Chatroom does not exist, create a new one
                    chatroomModel = Chatroom(
                        chatroomId,
                        listOf(FirebaseUtil.currentUserEmail()!!, selectedUser.email!!),
                        null
                    )
                    chatroomReference.setValue(chatroomModel)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ChatFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}
