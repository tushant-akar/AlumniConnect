package com.example.studentportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.example.studentportal.data.Users

class StudentRegistrationActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    lateinit var tvLogin: TextView
    var isRegistrationValid = true

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_registration)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        btnRegister.setOnClickListener {
            signUpUser()
            saveData()
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password cannot be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the user already exists
        checkIfUserExists(email, etMobile.text.toString())
    }

    private fun checkIfUserExists(email: String, mobile: String) {
        // Query the database to check if the email or mobile number already exists
        dbRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Email already exists, registration is not valid
                    Toast.makeText(this@StudentRegistrationActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                    isRegistrationValid = false
                } else {
                    // Email is not registered, check mobile number
                    checkIfMobileExists(mobile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors in the database query
                Toast.makeText(this@StudentRegistrationActivity, "Database error", Toast.LENGTH_SHORT).show()
                isRegistrationValid = false
            }
        })
    }

    private fun checkIfMobileExists(mobile: String) {
        // Query the database to check if the mobile number already exists
        dbRef.orderByChild("mobile").equalTo(mobile).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Mobile number already exists, registration is not valid
                    Toast.makeText(this@StudentRegistrationActivity, "Mobile number already registered", Toast.LENGTH_SHORT).show()
                    isRegistrationValid = false
                } else {
                    // Mobile number is not registered, proceed with Firebase registration
                    auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener(this@StudentRegistrationActivity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@StudentRegistrationActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@StudentRegistrationActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                                isRegistrationValid = false
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors in the database query
                Toast.makeText(this@StudentRegistrationActivity, "Database error", Toast.LENGTH_SHORT).show()
                isRegistrationValid = false
            }
        })
    }

    private fun saveData() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val mobile = etMobile.text.toString()

        if (name.isEmpty()) {
            etName.error = "Please enter your name"
            return
        }
        if(email.isEmpty()) {
            etEmail.error = "Please enter your email"
            return
        }
        if(mobile.isEmpty()) {
            etMobile.error = "Please enter your mobile number"
            return
        }
        if (!isRegistrationValid) {
            return
        }
        val studentID = dbRef.push().key!!
        val users = Users(studentID, name, email, mobile)

        dbRef.child(studentID).setValue(users)
            .addOnCompleteListener {
                Toast.makeText(this@StudentRegistrationActivity, "Data saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this@StudentRegistrationActivity, "Error saving data: ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
