package com.plantmate.plantmate.DAO

import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.objects.Plant
import kotlin.random.Random

class DataHelper {
    companion object{
        fun generateData():HashMap<String, ArrayList<Plant>>{
            var data : HashMap<String, ArrayList<Plant>> = HashMap<String, ArrayList<Plant>>()
            data["Cactaceae"] = generateArrayList()
            data["Asphodelaceae"] = generateArrayList()
            data["Araceae"] = generateArrayList()
            data["Rutaceae"] = generateArrayList()
            return data
        }
        fun dataGenerate(size:Int): List<Entry> {
            val entries = mutableListOf<Entry>()
            for(i in 0 until size)
                entries.add(Entry(i.toFloat(), Random.nextInt(100, 5001).toFloat()))

            return entries
        }
        fun generateArrayList():ArrayList<Plant>{
            var data: ArrayList<Plant> = ArrayList<Plant>()
            data.add(Plant(
                "1",
                "Hi",
                "Cultivar1",
                "Scientific1",
                "asdf",
                1))
            data.add(Plant(
                "2",
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2))
            data.add(Plant(
                "1",
                "Hi",
                "Cultivar1",
                "Scientific1",
                "asdf",
                1))
            data.add(
                Plant(
                    "2",
                    "Hi2",
                    "Cultivar2",
                    "Scientific2",
                    "asdf",
                    2)
            )

            return data
        }

//        var data = HashMap<String, ArrayList<Plant>>()
//        fun generateData():HashMap<String, ArrayList<Plant>>{
//            var mAuth = FirebaseAuth.getInstance()
//            data = HashMap<String, ArrayList<Plant>>()
//
//            val db = Firebase.firestore
//
//            val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
//            for (col in collectionList) {
//                val colRef =
//                    db.collection("users").document("${mAuth.currentUser?.uid}").collection(col)
//                genObjectArrayList(colRef, col)
//            }
//
//
//            return data
//        }
//        fun dataGenerate(size:Int): List<Entry> {
//            val entries = mutableListOf<Entry>()
//            for(i in 0 until size)
//                entries.add(Entry(i.toFloat(), Random.nextInt(100, 5001).toFloat()))
//
//            return entries
//        }
//        fun genObjectArrayList(collectionReference: CollectionReference, familyName: String){
//            var al = ArrayList<Plant>()
//            collectionReference.get().addOnSuccessListener {  result ->
//                result.forEach{
//                    al.add(Plant(
//                        it.id,
//                        it.data["plantFamily"].toString(),
//                        it.data["plantCultivarName"].toString(),
//                        it.data["plantScientificName"].toString(),
//                        it.data["plantDescription"].toString(),
//                        it.data["plantStock"].toString().toInt())
//                    )
//                }
//                data[familyName] = al
//                //notify
//
//            }
//        }
    }
}