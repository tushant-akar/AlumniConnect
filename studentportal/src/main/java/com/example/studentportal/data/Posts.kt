package com.example.studentportal.data

import java.sql.Timestamp

data class Posts(
    val userId: String,
    val name: String,
    val timestamp: String,
    val content: String
)
