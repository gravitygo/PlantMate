package com.plantmate.plantmate.DAO

import android.util.Log
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.plantmate.plantmate.CallBack.EntryCallback
import java.sql.Timestamp
import kotlin.random.Random

class TransactionDao {

    val db = FirebaseFirestore.getInstance()
    var min = Float.MAX_VALUE
    var max = Float.MIN_VALUE
    var mAuth = FirebaseAuth.getInstance()

    fun read(size: Long, callback: EntryCallback) {
        val entries = mutableListOf<Map<String, Any>>()
        var runningSum=0f
        db.collection("users")
            .document("${mAuth.uid}")
            .collection("transaction")
            .orderBy("date")
            .limit(size)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.forEach{doc ->
                    entries.add(doc.data!!)
                    val cost = doc.data!!["cost"] as Number
                    runningSum += cost.toFloat()
                    if(min > runningSum)
                        min = runningSum
                    if(max<runningSum)
                        max = runningSum
                }
                callback.onSuccess(entries)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception)
            }
    }
}