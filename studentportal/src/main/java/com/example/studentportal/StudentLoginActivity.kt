package com.example.studentportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentportal.student.StudentDashboardActivity
import com.example.studentportal.alumni.AlumniDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentLoginActivity : AppCompatActivity() {

    private lateinit var tvRedirectSignUp: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)

        tvRedirectSignUp = findViewById(R.id.tvRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }

        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, StudentRegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if(email.isBlank() || password.isBlank()) {
            Toast.makeText(this,"Email and Password cannot be blank",Toast.LENGTH_SHORT).show()
            return
        }

        // Inside the login function after task.isSuccessful check
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser

                    // Fetch additional user data from Firebase Realtime Database
                    // Replace "Users" with the actual reference to your database
                    val userReference = FirebaseDatabase.getInstance().getReference("Users")
                    currentUser?.uid?.let { userId ->
                        userReference.child(userId).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val isAlumni = dataSnapshot.child("isAlumni").getValue(Boolean::class.java) ?: false

                                Toast.makeText(this@StudentLoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                                // Redirect to the appropriate dashboard based on isAlumni
                                val intent = if (isAlumni) {
                                    Intent(this@StudentLoginActivity, AlumniDashboardActivity::class.java)
                                } else {
                                    Intent(this@StudentLoginActivity, StudentDashboardActivity::class.java)
                                }

                                startActivity(intent)
                                finish()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                                Toast.makeText(this@StudentLoginActivity, "Error fetching user data", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

    }
}
