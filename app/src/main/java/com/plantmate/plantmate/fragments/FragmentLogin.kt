package com.plantmate.plantmate.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentLoginBinding
import com.plantmate.plantmate.HomeActivity

class FragmentLogin: Fragment(R.layout.fragment_login){

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater)
        binding.fragmentLoginBtnLoginGoogle.setOnClickListener {
            val intent = Intent(container!!.context, HomeActivity::class.java)
            intent.putExtra("garden", "Chyle")
            startActivity(intent)
            finishAffinity(requireActivity())
        }
        return binding.root
    }
}