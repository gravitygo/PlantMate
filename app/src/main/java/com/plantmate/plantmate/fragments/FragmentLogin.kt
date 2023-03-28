package com.plantmate.plantmate.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.plantmate.plantmate.objects.FragmentUtils
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment

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

        binding.fragmentLoginTvSignup.setOnClickListener{
            replaceFragment(FragmentSignup(), R.id.activity_entry_fragment_view, parentFragmentManager)
        }

        binding.fragmentLoginBtnLogin.setOnClickListener{
            loginUser()
        }

        binding.fragmentLoginEtEmail.addTextChangedListener(textWatcher)
        binding.fragmentLoginEtPassword.addTextChangedListener(textWatcher)

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(isAllFieldsFilled()){
                binding.fragmentLoginBtnLogin.isEnabled = true
                binding.fragmentLoginBtnLogin.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.jungle_green))
            } else{
                binding.fragmentLoginBtnLogin.isEnabled = false
                binding.fragmentLoginBtnLogin.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
            }
        }
    }

    private fun isAllFieldsFilled(): Boolean {
        return binding.fragmentLoginEtEmail.text.isNotEmpty()
                && binding.fragmentLoginEtPassword.text.isNotEmpty()
    }


    private fun loginUser(){
        val email = binding.fragmentLoginEtEmail.text.toString()
        val password = binding.fragmentLoginEtPassword.text.toString()
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