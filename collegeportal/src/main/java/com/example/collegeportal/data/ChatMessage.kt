package com.example.collegeportal.data

import java.sql.Timestamp

data class ChatMessage(
    var message: String? = null,
    var senderId: String? = null,
    var timestamp: Timestamp? = null
)
