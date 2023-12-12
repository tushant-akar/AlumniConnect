package com.example.collegeportal.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.sql.Timestamp

object FirebaseUtil {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun currentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun isLoggedIn(): Boolean {
        return currentUserEmail() != null
    }

    fun currentUserDetails(): DatabaseReference {
        return database.reference.child("Users").child(currentUserEmail()!!.replace(".", "_")) // Adjusted for Firebase node key constraints
    }

    fun allUserDatabaseReference(): DatabaseReference {
        return database.reference.child("Users")
    }

    fun getChatroomReference(chatroomId: String): DatabaseReference {
        return database.reference.child("Chatrooms").child(chatroomId)
    }

    fun getChatroomMessageReference(chatroomId: String): DatabaseReference {
        return getChatroomReference(chatroomId).child("Chats")
    }

    fun getChatroomId(userEmail1: String, userEmail2: String): String {
        if (userEmail1.hashCode() < userEmail2.hashCode()) {
            return userEmail1+"_"+userEmail2 // Adjusted for Firebase node key constraints
        } else {
            return userEmail2+"_"+userEmail1 // Adjusted for Firebase node key constraints
        }
    }

    fun allChatroomDatabaseReference(): DatabaseReference {
        return database.reference.child("Chatrooms")
    }

    fun getOtherUserFromChatroom(userEmails: List<String>): DatabaseReference {
        return if (userEmails[0] == currentUserEmail()) {
            allUserDatabaseReference().child(userEmails[1].replace(".", "_")) // Adjusted for Firebase node key constraints
        } else {
            allUserDatabaseReference().child(userEmails[0].replace(".", "_")) // Adjusted for Firebase node key constraints
        }
    }

    fun timestampToString(timestamp: Timestamp?): String {
        // You need to implement this based on your timestamp format
        // For example:
        // val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // return dateFormat.format(Date(timestamp))
        return timestamp.toString()
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentProfilePicStorageRef(): StorageReference {
        return storage.reference.child("profile_pic").child(currentUserEmail()!!.replace(".", "_")) // Adjusted for Firebase node key constraints
    }

    fun getOtherProfilePicStorageRef(otherUserEmail: String): StorageReference {
        return storage.reference.child("profile_pic").child(otherUserEmail.replace(".", "_")) // Adjusted for Firebase node key constraints
    }
}