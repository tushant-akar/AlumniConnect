package com.example.collegeportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collegeportal.data.Users
import com.google.firebase.auth.FirebaseAuth

class CollegeLoginActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    lateinit var btnLogin: Button

    lateinit var auth: FirebaseAuth
    lateinit var users: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_college_login)

        etEmail = findViewById(R.id.clgLoginID)
        etPassword = findViewById(R.id.clgLoginPassword)
        btnLogin = findViewById(R.id.clgLoginButton)

        auth = FirebaseAuth.getInstance()

        fun collegeLogin() {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return
            }

            if (users.isStudent) {
                Toast.makeText(this, "Please login with valid ID", Toast.LENGTH_SHORT).show()
                return
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, CollegeDashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnLogin.setOnClickListener{
            collegeLogin()
        }
    }
}