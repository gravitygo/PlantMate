package com.plantmate.plantmate.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.plantmate.plantmate.HomeActivity
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentTopNavBinding

class FragmentTopNav(val color: Int): Fragment(R.layout.fragment_top_nav){

    private lateinit var binding: FragmentTopNavBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopNavBinding.inflate(inflater)

        binding.topPanel.setBackgroundColor(color)


        binding.backButton.setOnClickListener{

            val goToHome = Intent(container!!.context, HomeActivity::class.java)
            startActivity(goToHome)

        }
        return binding.root
    }
}