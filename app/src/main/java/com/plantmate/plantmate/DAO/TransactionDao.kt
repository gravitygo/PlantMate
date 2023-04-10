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
    var mAuth = FirebaseAuth.getInstance()

    fun read(col: String,callback: EntryCallback) {
        val entries = mutableListOf<Map<String, Any>>()
        db.collection("users")
            .document("${mAuth.uid}")
            .collection(col)
            .orderBy("date")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.forEach{doc ->
                    entries.add(doc.data!!)
                }
                callback.onSuccess(entries)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception)
            }
    }
}