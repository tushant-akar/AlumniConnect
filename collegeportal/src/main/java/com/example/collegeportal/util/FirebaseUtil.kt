package com.example.collegeportal.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.sql.Timestamp
import java.text.SimpleDateFormat

object FirebaseUtil {
    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    val isLoggedIn: Boolean
        get() = currentUserId() != null

    fun currentUserDetails(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users").child(currentUserId()!!)
    }

    fun allUserDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users")
    }

    fun getChatroomReference(chatroomId: String?): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId!!)
    }

    fun getChatroomMessageReference(chatroomId: String?): DatabaseReference {
        return getChatroomReference(chatroomId).child("chats")
    }

    fun getChatroomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) {
            userId1 + "_" + userId2
        } else {
            userId2 + "_" + userId1
        }
    }

    fun allChatroomDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("chatrooms")
    }

    fun getOtherUserFromChatroom(userIds: List<String>): DatabaseReference {
        return if (userIds[0] == currentUserId()) {
            allUserDatabaseReference().child(userIds[1])
        } else {
            allUserDatabaseReference().child(userIds[0])
        }
    }

    fun timestampToString(timestamp: Timestamp?): String {
        return SimpleDateFormat("HH:MM").format(timestamp)
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    val currentProfilePicStorageRef: StorageReference
        get() = FirebaseStorage.getInstance().reference.child("profile_pic")
            .child(currentUserId()!!)

    fun getOtherProfilePicStorageRef(otherUserId: String?): StorageReference {
        return FirebaseStorage.getInstance().reference.child("profile_pic")
            .child(otherUserId!!)
    }
}
