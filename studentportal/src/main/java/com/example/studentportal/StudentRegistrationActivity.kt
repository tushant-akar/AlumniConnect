package com.example.studentportal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentportal.alumni.AlumniDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.example.studentportal.data.Users
import com.example.studentportal.student.StudentDashboardActivity

class StudentRegistrationActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    lateinit var tvLogin: TextView
    var isRegistrationValid = true
    private lateinit var users: Users

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
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val mobile = etMobile.text.toString()
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

        // Check if the user has the domain "@iiitvadodara.ac.in"
        if (!email.endsWith("@iiitvadodara.ac.in")) {
            Toast.makeText(this, "Please use your college email id", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the user already exists
        checkIfUserExists(email, mobile)
    }

    private fun checkIfUserExists(email: String, mobile: String) {
        // Query the database to check if the email or mobile number already exists
        dbRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Email already exists, registration is not valid
                    Toast.makeText(this@StudentRegistrationActivity, "User already registered", Toast.LENGTH_SHORT).show()
                    isRegistrationValid = false
                } else {
                    // Email is not registered, check mobile number
                    checkIfMobileExists(mobile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors in the database query
                Log.e("DatabaseError", "Error querying database: ${error.message}")
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
                                Log.d("Registration", "Registration Confirmed")
                                saveData()
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
                Log.e("DatabaseError", "Error querying database: ${error.message}")
                Toast.makeText(this@StudentRegistrationActivity, "Database error", Toast.LENGTH_SHORT).show()
                isRegistrationValid = false
            }
        })
    }

    private fun saveData() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val mobile = etMobile.text.toString()

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isRegistrationValid) {
            return
        }

        val studentID = dbRef.push().key ?: return
        val newUsersData = Users(name, email, mobile, isStudent = true, isVerified = false, isAlumni = true)

        dbRef.child(studentID).setValue(newUsersData)
            .addOnCompleteListener {
                Toast.makeText(this@StudentRegistrationActivity, "Registration Confirmed", Toast.LENGTH_SHORT).show()
                Log.d("Registration", "Data saved successfully")
                if (newUsersData.isAlumni) {
                    // Navigate to Alumni Dashboard
                    val intent = Intent(this@StudentRegistrationActivity, AlumniDashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    // Navigate to Student Dashboard
                    val intent = Intent(this@StudentRegistrationActivity, StudentDashboardActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this@StudentRegistrationActivity, "Error saving data: ${err.message}", Toast.LENGTH_SHORT).show()
                Log.e("RegistrationError", "Error saving data: ${err.message}")
            }
    }
}
