package com.example.collegeportal.data

import java.sql.Timestamp

data class Chatroom(
    var chatroomId: String? = null,
    var usersIds: List<String>? = null,
    var lastMessageTimestamp: Timestamp? = null,
    var lastMessageSenderId: String? = null,
    var lastMessage: String? = null
)
