package com.plantmate.plantmate.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentLoginBinding
import com.plantmate.plantmate.HomeActivity

class FragmentLogin: Fragment(R.layout.fragment_login){

    private lateinit var binding : FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        binding = FragmentLoginBinding.inflate(inflater)
        binding.fragmentLoginBtnLoginGoogle.setOnClickListener {
            val intent = Intent(container!!.context, HomeActivity::class.java)
            intent.putExtra("garden", "Chyle")
            startActivity(intent)
            finishAffinity(requireActivity())
        }


        binding.fragmentLoginBtnLogin.setOnClickListener{
            loginUser()
        }

        return binding.root
    }

    private fun loginUser(){
        val email = binding.fragmentLoginEtEmail.text.toString()
        val password = binding.fragmentLoginEtPassword.text.toString()
        if(email.isEmpty()){
            binding.fragmentLoginEtEmail.requestFocus()
            Toast.makeText(activity,"Email cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()){
            binding.fragmentLoginEtPassword.requestFocus()
            Toast.makeText(activity,"Password cannot be empty", Toast.LENGTH_SHORT).show()
        } else{
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        val goToLogin = Intent(activity, HomeActivity::class.java)
                        startActivity(goToLogin)
                        finishAffinity(requireActivity())
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Login failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}