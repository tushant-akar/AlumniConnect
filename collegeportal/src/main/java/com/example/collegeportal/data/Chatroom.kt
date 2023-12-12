package com.example.collegeportal.data

import java.sql.Timestamp

data class Chatroom(
    var chatroomId: String? = null,
    var usersEmails: List<String>? = null,
    var lastMessageTimestamp: Timestamp? = null,
    var lastMessageSenderEmail: String? = null,
    var lastMessage: String? = null
)
