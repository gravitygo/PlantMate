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
        fun generateData(context: Activity): AdapterHome{
            var mAuth = FirebaseAuth.getInstance()
            val db = Firebase.firestore

            val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
            for (col in collectionList) {
                val colRef = db.collection("users").document("${mAuth.currentUser?.uid}").collection(col)
                Log.d("Gen Data ColRef ", colRef.toString())
                genObjectArrayList(colRef, col)
                Log.d("Gen Data Col", col)
            }

            return AdapterHome(data, context)
        }

        fun dataGenerate(size:Int): List<Entry> {
            val entries = mutableListOf<Entry>()
            for(i in 0 until size)
                entries.add(Entry(i.toFloat(), Random.nextInt(100, 5001).toFloat()))

            return entries
        }
        fun genObjectArrayList(collectionReference: CollectionReference, familyName: String){
            Log.d("Gen Object Array List", "called")
            var al = ArrayList<Plant>()
            collectionReference.get().addOnSuccessListener {  result ->
                result.forEach{
                    al.add(Plant(
                        it.id,
                        it.data["plantFamily"].toString(),
                        it.data["plantCultivarName"].toString(),
                        it.data["plantScientificName"].toString(),
                        it.data["plantDescription"].toString(),
                        it.data["plantStock"].toString().toInt())
                    )

                    Log.d(it.data["plantFamily"].toString(), it.data["plantScientificName"].toString())
                }
            }

            data[familyName] = al
        }

    }

}