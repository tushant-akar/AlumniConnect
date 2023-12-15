package com.example.collegeportal.data

import java.sql.Timestamp

data class Events(
    val eventName: String,
    val eventDescription: String,
    val eventDate: Timestamp
)
