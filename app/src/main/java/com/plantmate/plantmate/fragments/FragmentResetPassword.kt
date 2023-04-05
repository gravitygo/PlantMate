package com.plantmate.plantmate.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentResetPasswordBinding
import com.plantmate.plantmate.databinding.FragmentSignupBinding
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment

class FragmentResetPassword: Fragment(R.layout.fragment_reset_password)  {
    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentResetPasswordBinding.inflate(inflater)

        binding.fragmentResetEtEmail.addTextChangedListener(textWatcher)

        binding.fragmentResetBtnSend.setOnClickListener{
            Firebase.auth.sendPasswordResetEmail(binding.fragmentResetEtEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        binding.fragmentResetEtEmail.text.clear()
                        Toast.makeText(requireActivity(), "Email reset sent.", Toast.LENGTH_SHORT).show()
                    } else{
                        binding.fragmentResetEtEmail.text.clear()
                        Toast.makeText(requireActivity(), "Invalid email.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.fragmentResetTvLogin.setOnClickListener{
            replaceFragment(FragmentLogin(), R.id.activity_entry_fragment_view, parentFragmentManager)
        }

        binding.fragmentResetTvSignup.setOnClickListener{
            replaceFragment(FragmentSignup(), R.id.activity_entry_fragment_view, parentFragmentManager)
        }

        return binding.root
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(binding.fragmentResetEtEmail.text.length != 0){
                binding.fragmentResetBtnSend.isEnabled = true
                binding.fragmentResetBtnSend.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.jungle_green))
            } else{
                binding.fragmentResetBtnSend.isEnabled = false
                binding.fragmentResetBtnSend.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
            }
        }
    }

}