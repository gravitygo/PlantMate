package com.plantmate.plantmate.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.DAO.DataHelper
import com.plantmate.plantmate.R
import com.plantmate.plantmate.adapters.AdapterHome
import com.plantmate.plantmate.databinding.FragmentHomeBinding

class FragmentHome : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        val recyclerView: RecyclerView = binding.fragmentHomeRvItems

        //adapter
        recyclerView.adapter = AdapterHome(DataHelper.generateData(), requireActivity())

        //Layout Manager
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }
}