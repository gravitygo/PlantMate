package com.plantmate.plantmate.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.plantmate.plantmate.EditProfileActivity
import com.plantmate.plantmate.HomeActivity
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentSignupBinding

class FragmentSignup: Fragment(R.layout.fragment_signup)  {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentSignupBinding.inflate(inflater)

        binding.fragmentSignupBtnSignup.setOnClickListener{
            createUser()
        }

        return binding.root
    }

    private fun createUser(){
        val email = binding.fragmentSignupEtEmail.text.toString()
        val password = binding.fragmentSignupEtPassword.text.toString()
        if(email.isEmpty()){
            binding.fragmentSignupEtEmail.requestFocus()
            Toast.makeText(activity,"Email cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()){
            binding.fragmentSignupEtPassword.requestFocus()
            Toast.makeText(activity,"Password cannot be empty", Toast.LENGTH_SHORT).show()
        } else{
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity,"User creation successful.", Toast.LENGTH_SHORT).show()
                        val goToLogin = Intent(activity, HomeActivity::class.java)
                        startActivity(goToLogin)
                        finishAffinity(requireActivity())
                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "User creation failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}