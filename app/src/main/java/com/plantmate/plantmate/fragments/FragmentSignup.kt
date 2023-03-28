package com.plantmate.plantmate.fragments

import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.plantmate.plantmate.EditProfileActivity
import com.plantmate.plantmate.HomeActivity
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentEntryBinding
import com.plantmate.plantmate.databinding.FragmentSignupBinding
import com.plantmate.plantmate.objects.FragmentUtils
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment

class FragmentSignup: Fragment(R.layout.fragment_signup)  {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mContainer: ViewGroup
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        mContainer = container!!
        binding = FragmentSignupBinding.inflate(inflater)

        binding.fragmentSignupTvLogin.setOnClickListener{
            val containerHeight = mContainer.parent as ConstraintLayout
            val setHeight = containerHeight.layoutParams
            val fragmentView = R.id.activity_entry_fragment_view
            setHeight.height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 700F, resources.displayMetrics).toInt()
            containerHeight.layoutParams = setHeight
            replaceFragment(FragmentLogin(), fragmentView, parentFragmentManager)
        }

        binding.fragmentSignupEtGarden.addTextChangedListener(textWatcher)
        binding.fragmentSignupEtEmail.addTextChangedListener(textWatcher)
        binding.fragmentSignupEtPassword.addTextChangedListener(textWatcher)
        binding.fragmentSignupEtConfirmPassword.addTextChangedListener(textWatcher)

        binding.fragmentSignupBtnSignup.setOnClickListener{
            createUser()
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(isAllFieldsFilled()){
                binding.fragmentSignupBtnSignup.isEnabled = true
                binding.fragmentSignupBtnSignup.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.jungle_green))
            } else{
                binding.fragmentSignupBtnSignup.isEnabled = false
                binding.fragmentSignupBtnSignup.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
            }
        }
    }

    private fun isAllFieldsFilled(): Boolean {
        return binding.fragmentSignupEtGarden.text.isNotEmpty()
                && binding.fragmentSignupEtEmail.text.isNotEmpty()
                && binding.fragmentSignupEtPassword.text.isNotEmpty()
                && binding.fragmentSignupEtConfirmPassword.text.isNotEmpty()
    }

    private fun passwordsMatch(): Boolean {
        return if (binding.fragmentSignupEtPassword.text.toString() == binding.fragmentSignupEtConfirmPassword.text.toString()){
            true
        } else {
            binding.fragmentSignupEtPassword.text.clear()
            binding.fragmentSignupEtConfirmPassword.text.clear()
            Toast.makeText(activity, "Please match the passwords.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun passwordsLongEnough(): Boolean {
        return if (binding.fragmentSignupEtPassword.text.length >= 6){
            true
        } else {
            binding.fragmentSignupEtPassword.text.clear()
            binding.fragmentSignupEtConfirmPassword.text.clear()
            Toast.makeText(activity, "Please make sure passwords are at least 6 characters long.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun createUser(){
        val email = binding.fragmentSignupEtEmail.text.toString()
        val password = binding.fragmentSignupEtPassword.text.toString()
        if(passwordsMatch() && passwordsLongEnough()){
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity,"Sign up successful.", Toast.LENGTH_SHORT).show()

                        val containerHeight = mContainer.parent as ConstraintLayout
                        val setHeight = containerHeight.layoutParams
                        val fragmentView = R.id.activity_entry_fragment_view
                        setHeight.height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 700F, resources.displayMetrics).toInt()
                        containerHeight.layoutParams = setHeight
                        replaceFragment(FragmentLogin(), fragmentView, parentFragmentManager)

                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "User creation failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}