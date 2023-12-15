package com.example.collegeportal.data

import java.sql.Timestamp

data class Users(
    var name: String? = null,
    var email: String? = null,
    var mobileNumber: String? = null,
    var isStudent: Boolean = true,
    var isAlumni: Boolean = false,
    var isVerified: Boolean = false,
    var createdTimeStamp: Timestamp? = null,
    var userId: String? = null
)



