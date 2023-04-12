package com.plantmate.plantmate.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.R
import com.plantmate.plantmate.adapters.AdapterTransaction
import com.plantmate.plantmate.databinding.FragmentChartBinding
import com.plantmate.plantmate.databinding.FragmentTransactionBinding
import com.plantmate.plantmate.objects.Plant
import com.plantmate.plantmate.objects.TransactionData

class FragmentTransaction : Fragment(R.layout.fragment_transaction) {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterTransaction
    var data = ArrayList<TransactionData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater)
        recyclerView = binding.fragmentTransactionRv
        generateData()
        adapter = AdapterTransaction(data)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }
    fun generateData() {
        var mAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        db.collection("users")
            .document("${mAuth.currentUser?.uid}")
            .collection("transaction").orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener {
                for (docs in it) {
                    data.add(
                        TransactionData(
                            docs["date"] as Timestamp,
                            docs["cost"] as Number,
                            docs["plantId"] as String,
                            docs["type"] as String
                        )
                    )
                }
                adapter.notifyChange(data)
                recyclerView.adapter = adapter

            }
    }
}