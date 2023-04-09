package com.plantmate.plantmate.fragments

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.plantmate.plantmate.DAO.DataHelper
import com.plantmate.plantmate.R
import com.plantmate.plantmate.adapters.AdapterHome
import com.plantmate.plantmate.databinding.FragmentHomeBinding
import com.plantmate.plantmate.objects.Plant
import kotlinx.coroutines.*
import kotlin.random.Random

class FragmentHome : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterHome
    var data = HashMap<String, ArrayList<Plant>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        recyclerView = binding.fragmentHomeRvItems

        //adapter
        generateData()
//        recyclerView.adapter = DataHelper.generateData(requireActivity())
        adapter = AdapterHome(data, requireActivity())
        recyclerView.adapter = adapter
        //Layout Manager
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }
    fun generateData(){
        var mAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val collectionList = mutableListOf<String>()
        db.collection("users").document("${mAuth.currentUser?.uid}")
            .addSnapshotListener { result, exception ->
                if (exception != null) {
                    Log.w("TAG", "Error getting documents.", exception)
                    return@addSnapshotListener
                }

                if (result != null && result.exists()) {
                    val categoryData = result.get("categoryData") as Map<String, Any>
                    for (category in categoryData){
                        if (category.value as Boolean){
                            collectionList.add(category.key)
                        }
                    }

                    for (col in collectionList) {
                        val colRef = db.collection("users").document("${mAuth.currentUser?.uid}").collection(col)
                        colRef.addSnapshotListener { snapshot, error ->
                            if (error != null) {
                                Log.w("TAG", "Error getting documents.", error)
                                return@addSnapshotListener
                            }

                            val al = ArrayList<Plant>()
                            snapshot?.forEach{
                                al.add(Plant(
                                    it.id,
                                    it.data["plantFamily"].toString(),
                                    it.data["plantCultivarName"].toString(),
                                    it.data["plantScientificName"].toString(),
                                    it.data["plantDescription"].toString(),
                                    it.data["imageType"].toString(),
                                    it.data["plantStock"].toString().toInt())
                                )
                            }

                            data[col] = al
                            adapter.notifyChanged(data)
                            recyclerView.adapter = adapter
                        }
                    }
                } else {
                    Log.d("TAG", "Current data: null")
                }
            }
    }

}