package com.plantmate.plantmate.DAO

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.adapters.AdapterHome
import com.plantmate.plantmate.databinding.FragmentHomeBinding
import com.plantmate.plantmate.objects.Plant
import kotlin.random.Random

class DataHelper {
    companion object{
        var data = HashMap<String, ArrayList<Plant>>()
        fun dataGenerate(size:Int): List<Entry> {
            val entries = mutableListOf<Entry>()
            for(i in 0 until size)
                entries.add(Entry(i.toFloat(), Random.nextInt(100, 5001).toFloat()))

            return entries
        }
    }

}