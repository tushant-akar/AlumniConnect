package com.example.collegeportal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager

class UserVerificationFragment : Fragment() {
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvMobile: TextView
    private lateinit var btnVerify: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setValuesToViews()
        btnVerify.setOnClickListener {
            Log.d("UserVerificationFragment", "onButtonClicked")
            Toast.makeText(context, "User Verified", Toast.LENGTH_SHORT).show()
            navigateToFragment(parentFragmentManager, UsersFragment())
        }
    }

    fun navigateToFragment(fragmentManager: FragmentManager, fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun initView() {
        tvName = view?.findViewById(R.id.tvName)!!
        tvEmail = view?.findViewById(R.id.tvEmail)!!
        tvMobile = view?.findViewById(R.id.tvMobile)!!
        btnVerify = view?.findViewById(R.id.btnVerify)!!
    }

    private fun setValuesToViews() {
        val name = arguments?.getString("name")
        val email = arguments?.getString("email")
        val mobile = arguments?.getString("mobile")

        tvName.text = name
        tvEmail.text = email
        tvMobile.text = mobile
    }
}